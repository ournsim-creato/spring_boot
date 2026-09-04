package com.spring_boot_api_p2.feature.core.role.normalizer;

import com.spring_boot_api_p2.feature.core.role.dto.request.RoleRequest;
import org.springframework.stereotype.Component;

// Add ជា Bean
@Component
public class RoleNormalizer {

    // Call only this method
    public RoleRequest normalize(RoleRequest request) {
        request.setName(normalizeName(request.getName()));
        request.setDescription(normalizeDescription(request.getDescription()));
        return  request;
    }
    //Access Modifier
    // កាត់ space ខាងមុខ ខាងក្រោយ ប្តូទៅជាអគ្សរធំ
    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            return  null;
        }
        return name.trim().toUpperCase();
    }

    // កាត់ space ខាងមុខ ខាងក្រោយ
    private String normalizeDescription(String description) {
        if (description == null) {
            return  null;
        }
        return description.trim();
    }
}
