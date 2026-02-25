package com.base.cms.user.service;

import com.base.cms.common.constants.DelFlagConstants;
import com.base.cms.common.entities.SysDict;
import com.base.cms.common.exception.BadRequestException;
import com.base.cms.common.exception.ResourceNotFoundException;
import com.base.cms.user.dto.dict.SysDictQueryDto;
import com.base.cms.user.dto.dict.SysDictRequest;
import com.base.cms.user.dto.dict.SysDictResponse;
import com.base.cms.user.repository.SysDictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDictService {

    private final SysDictRepository sysDictRepository;

    @Transactional
    public SysDictResponse create(SysDictRequest request) {
        if (sysDictRepository.existsByDictType(request.getDictType())) {
            throw new BadRequestException("Dictionary type already exists");
        }

        SysDict dict = SysDict.builder()
                .dictType(request.getDictType())
                .description(request.getDescription())
                .build();

        SysDict saved = sysDictRepository.save(dict);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SysDictResponse> getAll() {
        return sysDictRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách SysDict có phân trang, lọc theo query (description, dictType) và loại trừ bản ghi đã xóa.
     * Gọi Repository.getDetails(query, delFlag, pageable).
     *
     * @param query    điều kiện tìm kiếm (description, dictType; null = không lọc)
     * @param pageable tham số phân trang (page, size, sort)
     * @return Page chứa danh sách SysDictResponse và thông tin phân trang
     */
    @Transactional(readOnly = true)
    public Page<SysDictResponse> getPage(SysDictQueryDto query, Pageable pageable) {
        SysDictQueryDto safeQuery = query != null ? query : new SysDictQueryDto();
        return sysDictRepository.getDetails(safeQuery, DelFlagConstants.DELETED, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public SysDictResponse getById(Long id) {
        SysDict dict = sysDictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
        return mapToResponse(dict);
    }

    @Transactional
    public SysDictResponse update(Long id, SysDictRequest request) {
        SysDict dict = sysDictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));

        if (!dict.getDictType().equals(request.getDictType())
                && sysDictRepository.existsByDictType(request.getDictType())) {
            throw new BadRequestException("Dictionary type already exists");
        }

        dict.setDictType(request.getDictType());
        dict.setDescription(request.getDescription());

        SysDict updated = sysDictRepository.save(dict);
        return mapToResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        SysDict dict = sysDictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
        sysDictRepository.delete(dict);
    }

    private SysDictResponse mapToResponse(SysDict dict) {
        return new SysDictResponse(
                dict.getId(),
                dict.getCreatedAt(),
                dict.getUpdatedAt(),
                dict.getDictType(),
                dict.getDescription()
        );
    }
}
