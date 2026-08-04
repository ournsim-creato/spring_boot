package com.spring_boot_api_p2.feature.core.role.dto.response;
import com.spring_boot_api_p2.feature.auth.dto.response.AuthResponse;
import com.spring_boot_api_p2.feature.core.role.user.response.UserResponse;
import lombok.Data;

@Data
public class RoleResponse {
    private Long id;
    private String name;
    private String description;

}