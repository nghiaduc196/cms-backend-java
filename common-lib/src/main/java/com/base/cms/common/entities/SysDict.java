package com.base.cms.common.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * System dictionary entity - Shared across all services
 */
@Entity
@Table(name = "sys_dict")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SysDict extends BaseAuditEntity {

    @Column(name = "dict_type", unique = true)
    @Schema(description = "Dictionary type")
    @Length(max = 100, message = "Dictionary type must be less than 100 characters")
    private String dictType;

    @Column(name = "description")
    @Schema(description = "Description")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
