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
 * System dictionary item entity - Shared across all services
 */
@Entity
@Table(name = "sys_dict_item")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SysDictItem extends BaseAuditEntity {

    @Column(name = "dict_id", nullable = false)
    @Schema(description = "Dictionary ID")
    private Long dictId;

    @Column(name = "item_value")
    @Schema(description = "Item value")
    @Length(max = 200, message = "Item value must be less than 200 characters")
    private String itemValue;

    @Column(name = "description")
    @Schema(description = "Description")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Column(name = "sort_order")
    @Schema(description = "Sort order")
    private Integer sortOrder;
}
