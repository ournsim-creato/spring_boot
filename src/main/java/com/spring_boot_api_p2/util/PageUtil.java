package com.spring_boot_api_p2.util;

import com.spring_boot_api_p2.dto.filter.BaseFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageUtil {

    private PageUtil() {
    }

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 30;

    public static Pageable getPageable(BaseFilter filter) {

        int page = filter.getPage() == null
                ? DEFAULT_PAGE
                : filter.getPage();

        int size = filter.getSize() == null
                ? DEFAULT_SIZE
                : filter.getSize();

        if (page < 1) {
            page = DEFAULT_PAGE;
        }

        if (size <= 0) {
            size = DEFAULT_SIZE;
        }

        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        int pageIndex = page - 1;


        if (filter.getSortBy() == null || filter.getSortBy().isBlank()) {
            return PageRequest.of(pageIndex, size);
        }


        Sort.Direction direction =
                "desc".equalsIgnoreCase(filter.getDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;


        return PageRequest.of(
                pageIndex,
                size,
                Sort.by(direction, filter.getSortBy())
        );
    }
}