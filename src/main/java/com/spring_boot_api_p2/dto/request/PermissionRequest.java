package com.spring_boot_api_p2.dto.request;

import lombok.Data;

@Data
public class PermissionRequest {

    private String name;
    private String description;
}