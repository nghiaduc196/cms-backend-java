package com.base.cms.user.service;

import com.base.cms.common.constants.DelFlagConstants;
import com.base.cms.common.constants.LockFlagConstants;
import com.base.cms.common.entities.SysDict;
import com.base.cms.common.entities.SysDictItem;
import com.base.cms.common.entities.SysDictItemTranslation;
import com.base.cms.common.exception.BadRequestException;
import com.base.cms.common.exception.ResourceNotFoundException;
import com.base.cms.user.dto.dictitem.SysDictItemRequest;
import com.base.cms.user.dto.dictitem.SysDictItemResponse;
import com.base.cms.user.dto.dictitem.SysDictItemTranslationRequest;
import com.base.cms.user.repository.SysDictItemRepository;
import com.base.cms.user.repository.SysDictItemTranslationRepository;
import com.base.cms.user.repository.SysDictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDictItemService {

    /** Locale mặc định khi client không truyền locale. */
    private static final String DEFAULT_LOCALE = "vi";

    private final SysDictItemRepository sysDictItemRepository;
    private final SysDictItemTranslationRepository sysDictItemTranslationRepository;
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

        if (request.getTranslations() == null || request.getTranslations().isEmpty()) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần gửi ít nhất một bản ghi trong translations (locale, description).");
        }
        boolean hasValidTranslation = request.getTranslations().stream()
                .anyMatch(t -> t != null && t.getLocale() != null && !t.getLocale().isBlank());
        if (!hasValidTranslation) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần ít nhất một bản ghi có locale hợp lệ trong translations.");
        }

        SysDictItem item = SysDictItem.builder()
                .dictId(request.getDictId())
                .itemValue(request.getItemValue())
                .sortOrder(request.getSortOrder())
                .build();

        SysDictItem saved = sysDictItemRepository.save(item);

        // Lưu từng bản dịch (đa ngôn ngữ gửi trong body: vi, en, ...)
        if (request.getTranslations() != null) {
            for (var t : request.getTranslations()) {
                if (t == null || t.getLocale() == null || t.getLocale().isBlank()) continue;
                SysDictItemTranslation trans = SysDictItemTranslation.builder()
                        .dictItemId(saved.getId())
                        .locale(t.getLocale().trim())
                        .description(t.getDescription())
                        .build();
                sysDictItemTranslationRepository.save(trans);
            }
        }

        return mapToResponse(saved, null);
    }

    @Transactional(readOnly = true)
    public List<SysDictItemResponse> getAll(String locale) {
        String effective = effectiveLocale(locale);
        return sysDictItemRepository.findAll().stream()
                .map(i -> mapToResponse(i, effective))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SysDictItemResponse getById(Long id, String locale) {
        SysDictItem item = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary item", "id", id));
        return mapToResponse(item, effectiveLocale(locale));
    }

    @Transactional(readOnly = true)
    public List<SysDictItemResponse> getByDictId(Long dictId, String locale) {
        String effective = effectiveLocale(locale);
        return sysDictItemRepository.findByDictIdOrderBySortOrderAsc(dictId).stream()
                .map(i -> mapToResponse(i, effective))
                .collect(Collectors.toList());
    }

    @Transactional
    public SysDictItemResponse update(Long id, SysDictItemRequest request) {
        SysDictItem item = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary item", "id", id));
        validateDictActive(request.getDictId());

        if (request.getTranslations() == null || request.getTranslations().isEmpty()) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần gửi ít nhất một bản ghi trong translations (locale, description).");
        }
        boolean hasValidTranslation = request.getTranslations().stream()
                .anyMatch(t -> t != null && t.getLocale() != null && !t.getLocale().isBlank());
        if (!hasValidTranslation) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần ít nhất một bản ghi có locale hợp lệ trong translations.");
        }

        item.setDictId(request.getDictId());
        item.setItemValue(request.getItemValue());
        item.setSortOrder(request.getSortOrder());

        SysDictItem updated = sysDictItemRepository.save(item);

        // Cập nhật từng bản dịch gửi trong body
        if (request.getTranslations() != null) {
            for (var t : request.getTranslations()) {
                if (t == null || t.getLocale() == null || t.getLocale().isBlank()) continue;
                SysDictItemTranslationRequest tr = new SysDictItemTranslationRequest();
                tr.setLocale(t.getLocale().trim());
                tr.setDescription(t.getDescription());
                saveTranslation(id, tr);
            }
        }

        return mapToResponse(updated, null);
    }

    @Transactional
    public void delete(Long id) {
        SysDictItem item = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary item", "id", id));
        sysDictItemTranslationRepository.deleteByDictItemId(id);
        sysDictItemRepository.delete(item);
    }

    /** Lưu hoặc cập nhật bản dịch theo locale cho một dictionary item. */
    @Transactional
    public void saveTranslation(Long dictItemId, SysDictItemTranslationRequest request) {
        sysDictItemRepository.findById(dictItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary item", "id", dictItemId));
        String locale = request.getLocale().trim();
        SysDictItemTranslation t = sysDictItemTranslationRepository.findByDictItemIdAndLocale(dictItemId, locale)
                .orElse(SysDictItemTranslation.builder().dictItemId(dictItemId).locale(locale).build());
        t.setDescription(request.getDescription());
        sysDictItemTranslationRepository.save(t);
    }

    /** Trả về locale hợp lệ: nếu null/blank thì dùng DEFAULT_LOCALE (vi). */
    private String effectiveLocale(String locale) {
        return (locale != null && !locale.isBlank()) ? locale.trim() : DEFAULT_LOCALE;
    }

    private SysDictItemResponse mapToResponse(SysDictItem item, String locale) {
        String description = null;
        if (locale != null && !locale.isBlank()) {
            description = sysDictItemTranslationRepository.findByDictItemIdAndLocale(item.getId(), locale.trim())
                    .filter(t -> t.getDescription() != null && !t.getDescription().isBlank())
                    .map(SysDictItemTranslation::getDescription)
                    .orElse(null);
        }
        return new SysDictItemResponse(
                item.getId(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getDictId(),
                item.getItemValue(),
                description,
                item.getSortOrder()
        );
    }
}
