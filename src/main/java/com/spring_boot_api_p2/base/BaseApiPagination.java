package com.spring_boot_api_p2.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring_boot_api_p2.dto.pagination.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BaseApiPagination<T> {
    private Boolean status;
    private Integer code;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private PaginationDTO pagination;

    private List<T> data;
}