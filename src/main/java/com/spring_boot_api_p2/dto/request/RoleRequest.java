package com.spring_boot_api_p2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class RoleRequest {
    @NotBlank(message = "Name cannot  blank")
    private String name;
    private String description;
    private String code;
    private  Integer num;
}