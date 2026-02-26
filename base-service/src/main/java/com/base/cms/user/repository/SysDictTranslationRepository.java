package com.base.cms.user.repository;

import com.base.cms.common.entities.SysDictTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysDictTranslationRepository extends JpaRepository<SysDictTranslation, Long> {

    Optional<SysDictTranslation> findByDictIdAndLocale(Long dictId, String locale);

    boolean existsByDictIdAndLocale(Long dictId, String locale);

    void deleteByDictId(Long dictId);
}
