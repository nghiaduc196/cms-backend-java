package com.base.cms.user.repository;

import com.base.cms.common.entities.SysDictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictItemRepository extends JpaRepository<SysDictItem, Long> {

    List<SysDictItem> findByDictIdOrderBySortOrderAsc(Long dictId);
}
