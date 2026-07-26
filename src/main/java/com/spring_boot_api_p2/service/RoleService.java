package com.spring_boot_api_p2.service;

import com.spring_boot_api_p2.dto.filter.RoleFilter;
import com.spring_boot_api_p2.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleImportResult;
import com.spring_boot_api_p2.dto.response.RoleResponse;
import com.spring_boot_api_p2.service.impl.RoleServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleResponse create (RoleRequest request);
    RoleResponse findById(Long id);
    RoleResponse update(Long id, RoleRequest request);
    void deleteById(Long id);
    List<RoleResponse> getAllFilter(RoleFilter  roleFilter);

    List<RoleResponse> getAllFilter(Map<String, String> params);

    Page<RoleResponse> getAllPagination(Map<String, String> params);

    RoleImportResult importFromXlsx(MultipartFile file);

    byte[] exportToXlsx();
    Page<RoleResponse> getAllPaginationFilter(RoleFilter filter);
}