package com.spring_boot_api_p2.feature.core.role.service;

import com.spring_boot_api_p2.dto.request.PermissionRequest;
import com.spring_boot_api_p2.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);

    PermissionResponse findById(Long id);

    List<PermissionResponse> findAll();

    PermissionResponse update(Long id, PermissionRequest request);

    void delete(Long id);
}