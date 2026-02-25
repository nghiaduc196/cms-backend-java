package com.base.cms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO containing dictionary item information")
public class SysDictItemResponse {
    @Schema(description = "Item ID", example = "1")
    private Long id;

    @Schema(description = "Dictionary ID", example = "1")
    private Long dictId;

    @Schema(description = "Item value", example = "ACTIVE")
    private String itemValue;

    @Schema(description = "Description", example = "Active status")
    private String description;

    @Schema(description = "Sort order", example = "1")
    private Integer sortOrder;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
