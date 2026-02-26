package com.base.cms.user.dto.dictitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Request DTO for creating or updating a dictionary item. Có thể gửi kèm translations (vi, en, ...) để lưu đa ngôn ngữ ngay khi tạo.")
public class SysDictItemRequest {

    @Schema(description = "Dictionary ID", example = "1", required = true)
    @NotNull(message = "Dictionary ID is required")
    private Long dictId;

    @Schema(description = "Item value (mã/giá trị)", example = "ACTIVE")
    @Length(max = 200, message = "Item value must be less than 200 characters")
    private String itemValue;

    @Schema(description = "Sort order", example = "1")
    private Integer sortOrder;

    @Schema(description = "Danh sách bản dịch theo locale. Gửi đầy đủ các ngôn ngữ đã setup (vi, en, ...) để lưu ngay khi tạo.")
    private List<@Valid DictItemLocaleDto> translations = new ArrayList<>();
}
