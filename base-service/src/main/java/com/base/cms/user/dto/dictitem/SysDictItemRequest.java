package com.base.cms.user.dto.dictitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Request DTO for creating or updating a dictionary item")
public class SysDictItemRequest {

    @Schema(description = "Dictionary ID", example = "1", required = true)
    @NotNull(message = "Dictionary ID is required")
    private Long dictId;

    @Schema(description = "Item value", example = "ACTIVE")
    @Length(max = 200, message = "Item value must be less than 200 characters")
    private String itemValue;

    @Schema(description = "Description", example = "Active status")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Schema(description = "Sort order", example = "1")
    private Integer sortOrder;
}
