package com.base.cms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO containing dictionary information")
public class SysDictResponse {
    @Schema(description = "Dictionary ID", example = "1")
    private Long id;

    @Schema(description = "Dictionary type", example = "USER_STATUS")
    private String dictType;

    @Schema(description = "Description", example = "User status dictionary")
    private String description;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
