package com.base.cms.common.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * Base audit entity with only basic audit fields (id, createdAt, updatedAt)
 * Use this when you don't need createdBy, updatedBy, or isDeleted fields
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseAuditEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 0-normal, 1-delete
     */
    @Column(name = "del_flag", columnDefinition = "CHAR")
    @Schema(description = "Delete mark, 1: deleted, 0: normal")
    @Length(max = 1, min = 1, message = "Delete mark must be 1 characters")
    private String delFlag;

    /**
     * Lock tag
     */
    @Column(name = "lock_flag", columnDefinition = "CHAR")
    @Schema(description = "Lock Mark, 9: locked, 0: normal")
    @Length(max = 1, min = 1, message = "Lock mark must be 1 characters")
    private String lockFlag;

    @Column(name = "system_flag")
    @Schema(description = "Whether the system is built-in")
    private String systemFlag;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
