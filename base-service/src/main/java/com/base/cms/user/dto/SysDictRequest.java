package com.base.cms.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Request DTO for creating or updating a dictionary")
public class SysDictRequest {
    @Schema(description = "Dictionary type", example = "USER_STATUS", required = true)
    @NotBlank(message = "Dictionary type is required")
    @Length(max = 100, message = "Dictionary type must be less than 100 characters")
    private String dictType;

    @Schema(description = "Description", example = "User status dictionary")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
