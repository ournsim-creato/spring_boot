package com.spring_boot_api_p2.dto.response;

import lombok.Builder;
import lombok.Data;


import java.util.List;

@Builder
@Data
public class RoleImportResult {
    private int totalRows;
    private int imported;
    private int skipped;
    private List<String> errors;

}
