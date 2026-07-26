package com.spring_boot_api_p2.specification;

import com.spring_boot_api_p2.dto.filter.RoleFilter;
import com.spring_boot_api_p2.domain.entity.Role;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;

public final class RoleSpecification {

    private RoleSpecification() {
    }

    // 3 fiels
    // 2 field
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final List<String> ALLOWED_SORT_FIELDS = List.of(FIELD_NAME, FIELD_ID);

    private static Specification<Role> hasName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get(FIELD_NAME)),
                    "%" + name.trim().toLowerCase() + "%"
            );
        };
    }


    /** WHERE clause: case-insensitive contains on name and/or code. */
    public static Specification<Role> build(RoleFilter filter) {
        if (filter == null) {
            return Specification.allOf(
                    hasName(null)
            );
        }

        return Specification.allOf(
                hasName(filter.getName())
        );
    }

    /** Pageable with validated sort field + direction. */
    public static Pageable pageable(RoleFilter filter) {
        int page = (filter != null && filter.getPage() != null) ? filter.getPage() : 0;
        int size = (filter != null && filter.getSize() != null) ? filter.getSize() : 20;

        String sortField = (filter != null && StringUtils.hasText(filter.getSortBy()))
                ? filter.getSortBy() : FIELD_ID;
        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }
        // asc desc
        boolean descending = filter != null && "desc".equalsIgnoreCase(filter.getDirection());
        Sort sort = descending ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        return PageRequest.of(page, size, sort);
    }
}