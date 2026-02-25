package com.base.cms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dùng cho truy vấn/tìm kiếm SysDict (phân trang, lọc theo description, dictType).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Query/filter DTO for dictionary search")
public class DictDTO {

    @Schema(description = "Filter by description (LIKE, optional)")
    private String description;

    @Schema(description = "Filter by dictionary type (LIKE, optional)")
    private String dictType;
}
