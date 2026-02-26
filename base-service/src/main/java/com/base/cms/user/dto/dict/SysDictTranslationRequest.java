package com.base.cms.user.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Request DTO for creating/updating a dictionary translation")
public class SysDictTranslationRequest {

    @Schema(description = "Locale code (vi, en, ja, ...)", example = "vi", required = true)
    @NotBlank(message = "Locale is required")
    @Length(max = 10, message = "Locale must be at most 10 characters")
    private String locale;

    @Schema(description = "Translated description", example = "Mô tả từ điển")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
