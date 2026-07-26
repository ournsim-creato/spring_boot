package com.spring_boot_api_p2.service.impl;

import com.spring_boot_api_p2.dto.filter.RoleFilter;
import com.spring_boot_api_p2.dto.response.RoleImportResult;
import com.spring_boot_api_p2.domain.entity.Role;
import com.spring_boot_api_p2.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleResponse;
import com.spring_boot_api_p2.exception.ResourceNotFoundException;
import com.spring_boot_api_p2.mapper.RoleMapper;
import com.spring_boot_api_p2.normalizer.RoleNormalizer;
import com.spring_boot_api_p2.repository.RoleRepository;
import com.spring_boot_api_p2.service.RoleService;

import com.spring_boot_api_p2.specification.RoleSpecification;
import com.spring_boot_api_p2.specification.RoleSpecificationOld;
import com.spring_boot_api_p2.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleNormalizer roleNormalizer;
    private final RoleValidator roleValidator;
    private final DataFormatter dataFormatter = new DataFormatter();

    @Override
    public RoleResponse create(RoleRequest request) {
        log.info("Create new Role with data: {}", request);

        // normalizer
        roleNormalizer.normalize(request);

        // validator
        roleValidator.validate(null, request);

        // map roleRequest (DTO) to Entity
        Role entity = roleMapper.toEntity(request);

        // save into data
        Role save = roleRepository.save(entity);

        // map Role entity to RoleResponse(DTO) and return
        return roleMapper.toResponse(save);
    }

    @Override
    public RoleResponse findById(Long id) {
        log.info("Attempting to find Role with ID: {}", id);
        return roleRepository.findById(id)
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

    }

    @Override
    public RoleResponse update(Long id, RoleRequest request) {
        log.info("Updating Role with ID: {}", id);

        // Normalizer
        roleNormalizer.normalize(request);

        // 1 data មានអត់
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

        //Validator
        roleValidator.validate(id, request);

        // map RoleRequest(DTO) to Entity
        roleMapper.updateEntity(role,request);

        //Update data
        Role save = roleRepository.save(role);

        // map Role entity to RoleResponse(DTO) and return
        return roleMapper.toResponse(save);
    }

    @Override
    public void deleteById(Long id) {
        // data មានឬអត់
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
        roleRepository.delete(role);

    }

    @Override
    public List<RoleResponse> getAllFilter(RoleFilter roleFilter) {
        return List.of();
    }

    @Override
    public List<RoleResponse> getAllFilter(Map<String, String> params) {
        Specification<Role> spec = RoleSpecificationOld.builderSpecification(params);

        return roleRepository.findAll(spec).stream()
                .map(roleMapper::toResponse)
                .toList();

    }

    @Override
    public Page<RoleResponse> getAllPagination(Map<String, String> params) {
//        log.info("Fetching paginated roles with params: {}", params);
//
//        int pageLimit = PageUtil.safeParse(params.get(PageUtil.PAGE_LIMIT), PageUtil.DEFAULT_PAGE_LIMIT);
//        int pageNumber = PageUtil.safeParse(params.get(PageUtil.PAGE_NUMBER), PageUtil.DEFAULT_PAGE_NUMBER);
//
//        Specification<Role> spec = RoleSpecification.builderSpecification(params);
//
//        Pageable pageable = PageUtil.getPageable(pageNumber, pageLimit);
//        log.info("Fetching Role with params: {}, pageNumber: {}, pageLimit: {}", params, pageNumber, pageLimit);
//
//        Page<Role> all = roleRepository.findAll(spec,pageable);
//
//        return all.map(roleMapper::toResponse);
        return  null;
    }

    @Transactional
    @Override
    public RoleImportResult importFromXlsx(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("XLSX file is required and must not be empty");
        }

        int total = 0;
        int imported = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();
        Set<String> seenInFile = new HashSet<>();

        // role.xlsx
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);

            Row header = sheet.getRow(0);

            if (header == null){
                throw new IllegalArgumentException("XLSX is empty (no header row)");
            }

            int nameCol = -1;
            int descCol = -1;

            for (Cell cell : header) {
                String h = cellString(cell);
                if (h == null) {
                    continue;
                }
                if (h.equalsIgnoreCase("name")) {
                    nameCol = cell.getColumnIndex();
                } else if (h.equalsIgnoreCase("description")) {
                    descCol = cell.getColumnIndex();
                }
            }
            if (nameCol == -1) {
                throw new IllegalArgumentException("XLSX must contain a 'name' column"); // -> 400
            }

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {          // iterate data rows (skip the header)
                Row row = sheet.getRow(1);                             // the row (may be null if blank)
                if (row == null) {                                    // fully-empty row?
                    continue;                                         // skip it, don't count it
                }
                total++;                                              // count this data row
                long line = r + 1;                                    // 1-based line number for messages

                RoleRequest request = new RoleRequest();              // reuse the normal create() DTO
                request.setName(cellString(row.getCell(nameCol)));    // read the name cell
//                request.setName(cellString(row.getCell(0)));    // read the name cell
                request.setDescription(descCol >= 0 ? cellString(row.getCell(descCol)) : null); // read description if present
                roleNormalizer.normalize(request);                   // same trim + uppercase as create()

                if (request.getName() == null) {                     // blank/missing name after normalizing?
                    skipped++;                                        // count as skipped
                    errors.add("row " + line + ": name is blank");   // record why
                    continue;                                        // next row
                }
                if (!seenInFile.add(request.getName())) {            // add() returns false if already in the file
                    skipped++;                                        // duplicate within this upload
                    errors.add("row " + line + ": '" + request.getName() + "' duplicated within the file"); // record why
                    continue;                                        // next row
                }
                if (roleRepository.existsByName(request.getName())) { // already in the database?
                    skipped++;                                        // skip existing rows
                    errors.add("row " + line + ": '" + request.getName() + "' already exists"); // record why
                    continue;                                        // next row
                }

                Role entity = roleMapper.toEntity(request);          // DTO -> entity (name + description)

                roleRepository.save(entity);                         // persist the new role
                imported++;                                          // count success
            }
        } catch (IOException e) {                                    // stream/parse failure
            throw new IllegalArgumentException("Failed to read XLSX file: " + e.getMessage()); // -> 400
        }

        return RoleImportResult.builder()                            // build the summary response
                .totalRows(total)                                    // rows read
                .imported(imported)                                  // rows saved
                .skipped(skipped)                                    // rows skipped
                .errors(errors)                                      // per-row messages
                .build();
    }

    @Override
    public byte[] exportToXlsx() {

        // fetch data
        List<Role> roles = roleRepository.findAll();
        // fetch matching roles

        try (Workbook workbook = new XSSFWorkbook();                     // new empty .xlsx workbook (auto-closed)
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {  // collect the workbook bytes in memory

            Sheet sheet = workbook.createSheet("roles");                 // create the "roles" sheet

            Row header = sheet.createRow(0);                             // first row = header
            header.createCell(0).setCellValue("name");                   // column A header
            header.createCell(1).setCellValue("description");            // column B header

            int rowIdx = 1;                                              // data starts on row index 1
            for (Role role : roles) {                                    // one spreadsheet row per role
                Row row = sheet.createRow(rowIdx++);                     // create the row, then advance
                row.createCell(0).setCellValue(role.getName() == null ? "" : role.getName());          // A: name
                row.createCell(1).setCellValue(role.getDescription() == null ? "" : role.getDescription()); // B: description
            }

//            sheet.setColumnWidth(0, 25 * 256);                           // widen column A (~25 chars; width unit = 1/256 char)
//            sheet.setColumnWidth(1, 50 * 256);                           // widen column B (~50 chars)

            workbook.write(out);                                         // serialize the workbook into the stream
            log.info("Role XLSX export: {} rows", roles.size());        // audit log
            return out.toByteArray();                                    // hand back the .xlsx bytes for download
        } catch (IOException e) {                                        // writing shouldn't normally fail
            throw new IllegalStateException("Failed to write XLSX: " + e.getMessage());
        }

    }

    @Override
    public Page<RoleResponse> getAllPaginationFilter(RoleFilter filter) {
        Specification<Role> spec = RoleSpecification.build(filter);
        Pageable pageable = RoleSpecification.pageable(filter);

        Page<Role> all = roleRepository.findAll(spec,pageable);

        System.out.println(all);

        return all.map(roleMapper::toResponse);

    }

    private String cellString(Cell cell) {
        if (cell == null) {
            return  null;
        }
        String value = dataFormatter.formatCellValue(cell);
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    // research Export
}