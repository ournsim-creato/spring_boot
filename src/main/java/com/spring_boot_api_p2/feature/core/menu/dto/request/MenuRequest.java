package com.spring_boot_api_p2.feature.core.menu.dto.request;

import lombok.Data;

@Data
public class MenuRequest {

    private String name;

    private String path;

    private String redirect;

    private Boolean alwaysShow;

    private Boolean hidden;

    private String title;

    private String icon;

    private Boolean noCache;

    private String titleKey;

    private String link;

    private String component;

    private Integer sortOrder;

    private Long parentId;
}
