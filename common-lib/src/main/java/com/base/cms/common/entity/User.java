package com.base.cms.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * User entity - Shared across all services
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseAuditEntity {
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;
}
