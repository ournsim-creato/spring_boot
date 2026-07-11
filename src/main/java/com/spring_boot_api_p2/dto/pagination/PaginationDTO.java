package com.spring_boot_api_p2.dto.pagination;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationDTO {

    private boolean first;
    private boolean last;
    private int pageSize;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
    private boolean empty;
}
