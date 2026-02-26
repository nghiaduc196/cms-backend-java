package com.base.cms.user.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Request DTO for creating or updating a dictionary. Có thể gửi kèm translations (vi, en, ...) để lưu đa ngôn ngữ ngay khi tạo.")
public class SysDictRequest {
    @Schema(description = "Dictionary type", example = "USER_STATUS", required = true)
    @NotBlank(message = "Dictionary type is required")
    @Length(max = 100, message = "Dictionary type must be less than 100 characters")
    private String dictType;

    @Schema(description = "Description mặc định (dùng nếu không gửi translations, hoặc làm fallback)")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Schema(description = "Danh sách bản dịch theo locale. Gửi đầy đủ các ngôn ngữ đã setup (vi, en, ...) để lưu ngay khi tạo.")
    private List<@Valid DictLocaleDto> translations = new ArrayList<>();
}
