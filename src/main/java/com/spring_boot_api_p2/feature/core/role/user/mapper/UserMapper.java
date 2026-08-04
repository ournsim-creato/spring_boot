package com.spring_boot_api_p2.feature.core.role.user.mapper;


import com.spring_boot_api_p2.domain.entity.Role;
import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.feature.core.role.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(
            target = "roles",
            source = "roles"
    )
    UserResponse toResponse(User user);



    default Set<String> map(Set<Role> roles) {

        if (roles == null) {
            return Set.of();
        }

        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}