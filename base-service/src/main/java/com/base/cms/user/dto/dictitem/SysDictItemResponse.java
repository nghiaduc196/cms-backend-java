package com.base.cms.user.dto.dictitem;

import com.base.cms.user.dto.common.BaseAuditResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Response DTO containing dictionary item information")
public class SysDictItemResponse extends BaseAuditResponseDto {

    @Schema(description = "Dictionary ID", example = "1")
    private Long dictId;

    /** Constructor đầy đủ (parent audit + trường nghiệp vụ) dùng cho mapToResponse. */
    public SysDictItemResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
                               Long dictId, String itemValue, String description, Integer sortOrder) {
        super(id, createdAt, updatedAt);
        this.dictId = dictId;
        this.itemValue = itemValue;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    @Schema(description = "Item value", example = "ACTIVE")
    private String itemValue;

    @Schema(description = "Description", example = "Active status")
    private String description;

    @Schema(description = "Sort order", example = "1")
    private Integer sortOrder;
}
