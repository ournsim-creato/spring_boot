package com.spring_boot_api_p2.service;

import com.spring_boot_api_p2.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleResponse create (RoleRequest request);
    RoleResponse findById(Long id);
    RoleResponse update(Long id, RoleRequest request);
    void deleteById(Long id);
    List<RoleResponse> getAll();
    List<RoleResponse> getAllFilter(Map<String, String> params);
    Page<RoleResponse> getAllPagination(Map<String, String> params);
}
// hidden impl