package com.spring_boot_api_p2.mapper;

import com.spring_boot_api_p2.domain.entity.Role;
import com.spring_boot_api_p2.feature.core.role.dto.request.RoleRequest;
import com.spring_boot_api_p2.feature.core.role.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    Role toEntity(RoleRequest request);

    RoleResponse toResponse(Role role);

    void updateEntity(@MappingTarget Role role, RoleRequest request);
}
