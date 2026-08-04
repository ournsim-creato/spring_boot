package com.spring_boot_api_p2.feature.core.role.service;

import com.spring_boot_api_p2.feature.core.role.dto.filter.RoleFilter;
import com.spring_boot_api_p2.feature.core.role.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleImportResult;
import com.spring_boot_api_p2.feature.core.role.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoleService {
    RoleResponse create (RoleRequest request);
    RoleResponse findById(Long id);
    RoleResponse update(Long id, RoleRequest request);
    void deleteById(Long id);
    List<RoleResponse> getAllFilter(RoleFilter filter);

    RoleImportResult importFromXlsx(MultipartFile file);

    byte[] exportToXlsx();

    Page<RoleResponse> getAllPaginationFilter(RoleFilter filter);

}