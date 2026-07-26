package com.spring_boot_api_p2.dto.filter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseFilter implements PageSortFilter {
    private String sortBy;
    // asc, desc
    private String direction;
    private Integer page;
    private Integer size;
}