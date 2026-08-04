package com.spring_boot_api_p2.feature.core.role.dto.filter;


import com.spring_boot_api_p2.dto.filter.BaseFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleFilter extends BaseFilter {
    private String code;
    private String name;
}