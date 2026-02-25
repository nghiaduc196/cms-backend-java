package com.base.cms.user.dto.user;

import com.base.cms.user.dto.common.BaseAuditResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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

    /** Constructor đầy đủ (parent audit + trường nghiệp vụ) dùng cho mapToResponse. */
    public UserResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String email, String name) {
        super(id, createdAt, updatedAt);
        this.email = email;
        this.name = name;
    }
}
