package com.spring_boot_api_p2.dto.filter;

public interface PageSortFilter {

    String getSortBy();
    String getDirection();
    Integer getPage();
    Integer getSize();
}
