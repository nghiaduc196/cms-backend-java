package com.base.cms.user.dto.user;

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
@Schema(description = "Response DTO containing user information")
public class UserResponse extends BaseAuditResponseDto {

    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @Schema(description = "User full name", example = "John Doe")
    private String name;
}
