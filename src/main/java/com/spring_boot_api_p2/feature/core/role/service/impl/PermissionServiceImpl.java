package com.spring_boot_api_p2.feature.core.role.service.impl;

import com.spring_boot_api_p2.feature.core.permission.dto.request.PermissionRequest;
import com.spring_boot_api_p2.feature.core.permission.dto.response.PermissionResponse;
import com.spring_boot_api_p2.feature.core.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    @Override
    public PermissionResponse create(PermissionRequest request) {
        return null;
    }

    @Override
    public PermissionResponse findById(Long id) {
        return null;
    }

    @Override
    public List<PermissionResponse> findAll() {
        return List.of();
    }

    @Override
    public PermissionResponse update(Long id, PermissionRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
