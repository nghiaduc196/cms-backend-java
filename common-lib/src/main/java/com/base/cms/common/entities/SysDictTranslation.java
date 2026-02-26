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
 * Bản dịch đa ngôn ngữ cho SysDict (description theo locale).
 */
@Entity
@Table(name = "sys_dict_translation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dict_id", "locale"})
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SysDictTranslation extends BaseAuditEntity {

    @Column(name = "dict_id", nullable = false)
    @Schema(description = "Dictionary ID")
    private Long dictId;

    @Column(name = "locale", nullable = false, length = 10)
    @Schema(description = "Locale code (vi, en, ja, ...)")
    @Length(max = 10, message = "Locale must be at most 10 characters")
    private String locale;

    @Column(name = "description", length = 500)
    @Schema(description = "Translated description")
    @Length(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
