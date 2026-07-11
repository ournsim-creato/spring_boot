package com.spring_boot_api_p2.mapper;

import com.spring_boot_api_p2.domain.entity.Role;
import com.spring_boot_api_p2.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleRequest request);

    RoleResponse toResponse(Role permission);

    void updateEntity(@MappingTarget Role target, RoleRequest request);
}