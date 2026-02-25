package com.base.cms.user.repository;

import com.base.cms.common.entities.SysDict;
import com.base.cms.user.dto.DictDTO;
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
     * Lấy danh sách SysDict có phân trang, lọc theo delFlag và điều kiện tìm kiếm (description, dictType).
     * Loại trừ bản ghi đã xóa (delFlag = "1"), tùy chọn LIKE theo description hoặc dictType khi query truyền vào.
     */
    @Query(value = "SELECT s FROM SysDict s WHERE s.delFlag <> :delFlag " +
            "AND ((:#{#query.description} IS NULL OR s.description LIKE CONCAT(CONCAT('%', :#{#query.description}), '%')) " +
            "OR (:#{#query.dictType} IS NULL OR s.dictType LIKE CONCAT(CONCAT('%', :#{#query.dictType}), '%')))")
    Page<SysDict> getDetails(@Param("query") DictDTO query, @Param("delFlag") String delFlag, Pageable pageable);
}
