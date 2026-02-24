package com.base.cms.user.repository;

import com.base.cms.common.entities.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SysUser, Long> {
    Optional<SysUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
