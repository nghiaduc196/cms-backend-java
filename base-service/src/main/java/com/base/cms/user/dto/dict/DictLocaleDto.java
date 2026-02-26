package com.base.cms.user.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Dữ liệu mô tả theo từng locale khi tạo/cập nhật SysDict (đa ngôn ngữ trong một request).
 */
@Data
@Schema(description = "Dictionary description per locale")
public class DictLocaleDto {

    @Schema(description = "Locale code (vi, en, ...)", example = "vi", required = true)
    @NotBlank(message = "Locale is required")
    @Length(max = 10)
    private String locale;

    @Schema(description = "Translated description", example = "Mô tả từ điển")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
