package com.base.cms.user.repository;

import com.base.cms.common.entities.SysDict;
import com.base.cms.user.dto.dict.SysDictQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysDictRepository extends JpaRepository<SysDict, Long> {

    Optional<SysDict> findByDictType(String dictType);

    boolean existsByDictType(String dictType);

    /**
     * Lấy danh sách SysDict có phân trang, lọc theo delFlag và dictType.
     * Loại trừ bản ghi đã xóa (delFlag = "1"). Description không còn trên entity (nằm ở bảng sys_dict_translation) nên chỉ lọc theo dictType.
     */
    @Query(value = "SELECT s FROM SysDict s WHERE s.delFlag <> :delFlag " +
            "AND (:#{#query.dictType} IS NULL OR s.dictType LIKE CONCAT(CONCAT('%', :#{#query.dictType}), '%'))")
    Page<SysDict> getDetails(@Param("query") SysDictQueryDto query, @Param("delFlag") String delFlag, Pageable pageable);
}
