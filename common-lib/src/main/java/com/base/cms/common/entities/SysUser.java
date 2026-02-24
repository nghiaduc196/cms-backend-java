package com.base.cms.common.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.base.cms.common.constants.CommonConstants;
import org.hibernate.validator.constraints.Length;

/**
 * System user entity - Shared across all services
 */
@Entity
@Table(name = "sys_user")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SysUser extends BaseAuditEntity {

    /**
     * username
     */
    @Column(name = "username", unique = true)
    @Schema(description = "username")
    @NotBlank(message = "username cannot be blank")
    @Length(max = 100, message = "username must be less than 100 characters")
    private String username;

    /**
     * password
     */
    @Column(name = "password")
    @Schema(description = "password")
    @Length(max = 255, message = "password must be less than 255 characters")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @Schema(description = "Email")
    @Length(max = 100, message = "Email must be less than 100 characters")
    @Email(regexp = CommonConstants.EMAIL_PATTERN, message = "Enter a valid email address")
    private String email;

    @Column(name = "name", nullable = false)
    @Schema(description = "Name")
    @Length(max = 100, message = "Name must be less than 100 characters")
    private String name;
}
