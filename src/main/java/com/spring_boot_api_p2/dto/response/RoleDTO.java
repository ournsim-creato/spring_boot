package com.spring_boot_api_p2.dto.response;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String description;
    private String isDeleted;
}
