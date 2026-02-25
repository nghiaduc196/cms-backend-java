package com.base.cms.user.service;

import com.base.cms.common.constants.DelFlagConstants;
import com.base.cms.common.constants.LockFlagConstants;
import com.base.cms.common.entities.SysDict;
import com.base.cms.common.entities.SysDictItem;
import com.base.cms.common.exception.BadRequestException;
import com.base.cms.common.exception.ResourceNotFoundException;
import com.base.cms.user.dto.SysDictItemRequest;
import com.base.cms.user.dto.SysDictItemResponse;
import com.base.cms.user.repository.SysDictItemRepository;
import com.base.cms.user.repository.SysDictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDictItemService {

    private final SysDictItemRepository sysDictItemRepository;
    private final SysDictRepository sysDictRepository;

    /**
     * Kiểm tra dictionary có đang hoạt động không (chưa xóa, chưa khóa).
     * Ném BadRequestException nếu đã xóa hoặc không hoạt động.
     */
    private void validateDictActive(Long dictId) {
        SysDict dict = sysDictRepository.findById(dictId)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", dictId));
        if (DelFlagConstants.DELETED.equals(dict.getDelFlag())) {
            throw new BadRequestException("Dictionary is deleted and cannot be used");
        }
        if (LockFlagConstants.LOCKED.equals(dict.getLockFlag())) {
            throw new BadRequestException("Dictionary is not active (locked)");
        }
    }

    @Transactional
    public SysDictItemResponse create(SysDictItemRequest request) {
        validateDictActive(request.getDictId());

        SysDictItem item = SysDictItem.builder()
                .dictId(request.getDictId())
                .itemValue(request.getItemValue())
                .description(request.getDescription())
                .sortOrder(request.getSortOrder())
                .build();

        SysDictItem saved = sysDictItemRepository.save(item);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SysDictItemResponse> getAll() {
        return sysDictItemRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SysDictItemResponse getById(Long id) {
        SysDictItem item = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary item", "id", id));
        return mapToResponse(item);
    }

    @Transactional(readOnly = true)
    public List<SysDictItemResponse> getByDictId(Long dictId) {
        return sysDictItemRepository.findByDictIdOrderBySortOrderAsc(dictId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SysDictItemResponse update(Long id, SysDictItemRequest request) {
        SysDictItem item = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary item", "id", id));
        validateDictActive(request.getDictId());

        item.setDictId(request.getDictId());
        item.setItemValue(request.getItemValue());
        item.setDescription(request.getDescription());
        item.setSortOrder(request.getSortOrder());

        SysDictItem updated = sysDictItemRepository.save(item);
        return mapToResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        SysDictItem item = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary item", "id", id));
        sysDictItemRepository.delete(item);
    }

    private SysDictItemResponse mapToResponse(SysDictItem item) {
        return new SysDictItemResponse(
                item.getId(),
                item.getDictId(),
                item.getItemValue(),
                item.getDescription(),
                item.getSortOrder(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
