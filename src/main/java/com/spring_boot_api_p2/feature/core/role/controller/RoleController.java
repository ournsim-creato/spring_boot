package com.spring_boot_api_p2.feature.core.role.controller;

import com.spring_boot_api_p2.base.BaseApi;
import com.spring_boot_api_p2.base.BaseApiPagination;
import com.spring_boot_api_p2.feature.core.role.dto.filter.RoleFilter;
import com.spring_boot_api_p2.dto.pagination.PageDTO;
import com.spring_boot_api_p2.feature.core.role.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleImportResult;
import com.spring_boot_api_p2.feature.core.role.dto.response.RoleResponse;
import com.spring_boot_api_p2.feature.core.role.service.RoleService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    private final RoleService roleService;

    RoleController(RoleService roleServiceInject) {
        this.roleService = roleServiceInject;
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        RoleResponse role = roleService.create(request);
        return  ResponseEntity.ok(role);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        RoleResponse response = roleService.findById(id);
        return ResponseEntity.ok(
                BaseApi.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RoleRequest request){
        RoleResponse response = roleService.update(id, request);

        return ResponseEntity.ok(
                BaseApi.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllFilterByName(RoleFilter filter){
        List<RoleResponse> response = roleService.getAllFilter(filter);
        return  ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<?> paginationFiler(RoleFilter roleFilter){
        Page<RoleResponse> allPagination = roleService.getAllPaginationFilter(roleFilter);
        PageDTO pageDTO = new PageDTO(allPagination);

        return  ResponseEntity.ok(
                BaseApiPagination.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .pagination(pageDTO.getPagination())
                        .data((List<RoleResponse>) pageDTO.getItems())
                        .build()
        );
    }

    @PostMapping(value = "import-xlsx", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoleImportResult> importXlsx(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(roleService.importFromXlsx(file));
    }

    @GetMapping("export-xlsx")
    public ResponseEntity<byte[]> exportXlsx() {
        byte[] xlsx = roleService.exportToXlsx();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"roles.xlsx\"")
                .body(xlsx);
    }
}