package com.base.cms.user.dto.dict;

import com.base.cms.user.dto.common.BaseAuditResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Response DTO containing dictionary information")
public class SysDictResponse extends BaseAuditResponseDto {

    @Schema(description = "Dictionary type", example = "USER_STATUS")
    private String dictType;

    @Schema(description = "Description", example = "User status dictionary")
    private String description;
}
