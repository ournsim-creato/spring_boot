package com.spring_boot_api_p2.mapper;

import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.feature.core.user.dto.response.UserResponse;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface UserMapper {
    // បន្ថែម Mapping នេះដើម្បីបញ្ជាក់ប្រាប់ឱ្យច្បាស់
    @Mapping(source = "profileImage", target = "profile")
    UserResponse toResponse(User user);
}