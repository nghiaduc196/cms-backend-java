package com.base.cms.user.repository;

import com.base.cms.common.entities.SysDictItemTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysDictItemTranslationRepository extends JpaRepository<SysDictItemTranslation, Long> {

    Optional<SysDictItemTranslation> findByDictItemIdAndLocale(Long dictItemId, String locale);

    boolean existsByDictItemIdAndLocale(Long dictItemId, String locale);

    void deleteByDictItemId(Long dictItemId);
}
