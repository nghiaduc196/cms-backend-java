package com.base.cms.user.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base DTO cho response có trường audit (id, createdAt, updatedAt).
 * Các response DTO kế thừa và bổ sung trường nghiệp vụ.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Base response DTO with audit fields")
public abstract class BaseAuditResponseDto {

    @Schema(description = "Primary key ID", example = "1")
    private Long id;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
