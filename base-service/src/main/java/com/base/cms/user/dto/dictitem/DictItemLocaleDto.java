package com.base.cms.user.dto.dictitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Dữ liệu description theo từng locale khi tạo/cập nhật SysDictItem (đa ngôn ngữ trong một request).
 */
@Data
@Schema(description = "Dictionary item translation per locale (description only)")
public class DictItemLocaleDto {

    @Schema(description = "Locale code (vi, en, ...)", example = "vi", required = true)
    @NotBlank(message = "Locale is required")
    @Length(max = 10)
    private String locale;

    @Schema(description = "Translated description", example = "Trạng thái đang hoạt động")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
