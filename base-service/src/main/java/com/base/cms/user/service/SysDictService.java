package com.base.cms.user.service;

import com.base.cms.common.constants.DelFlagConstants;
import com.base.cms.common.entities.SysDict;
import com.base.cms.common.entities.SysDictTranslation;
import com.base.cms.common.exception.BadRequestException;
import com.base.cms.common.exception.ResourceNotFoundException;
import com.base.cms.user.dto.dict.SysDictQueryDto;
import com.base.cms.user.dto.dict.SysDictRequest;
import com.base.cms.user.dto.dict.SysDictResponse;
import com.base.cms.user.dto.dict.DictLocaleDto;
import com.base.cms.user.dto.dict.SysDictTranslationRequest;
import com.base.cms.user.repository.SysDictRepository;
import com.base.cms.user.repository.SysDictTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class SysDictService {

    /** Locale mặc định khi client không truyền locale. */
    private static final String DEFAULT_LOCALE = "vi";

    /** Các trường SysDict được phép dùng trong sort (entity + BaseAuditEntity). Trường "string" map sang dictType. */
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "createdAt", "updatedAt", "dictType");
    private static final String SORT_DEFAULT = "id";
    private static final Sort.Direction SORT_DEFAULT_DIRECTION = Sort.Direction.DESC;

    private final SysDictRepository sysDictRepository;
    private final SysDictTranslationRepository sysDictTranslationRepository;

    @Transactional
    public SysDictResponse create(SysDictRequest request) {
        if (sysDictRepository.existsByDictType(request.getDictType())) {
            throw new BadRequestException("Dictionary type already exists");
        }

        if (request.getTranslations() == null || request.getTranslations().isEmpty()) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần gửi ít nhất một bản ghi trong translations (locale, description).");
        }
        boolean hasValidTranslation = request.getTranslations().stream()
                .anyMatch(t -> t != null && t.getLocale() != null && !t.getLocale().isBlank());
        if (!hasValidTranslation) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần ít nhất một bản ghi có locale hợp lệ trong translations.");
        }

        SysDict dict = SysDict.builder()
                .dictType(request.getDictType())
                .build();

        SysDict saved = sysDictRepository.save(dict);

        // Lưu từng bản dịch (đa ngôn ngữ gửi trong body: vi, en, ...)
        if (request.getTranslations() != null) {
            for (DictLocaleDto t : request.getTranslations()) {
                if (t == null || t.getLocale() == null || t.getLocale().isBlank()) continue;
                SysDictTranslation trans = SysDictTranslation.builder()
                        .dictId(saved.getId())
                        .locale(t.getLocale().trim())
                        .description(t.getDescription())
                        .build();
                sysDictTranslationRepository.save(trans);
            }
        }

        return mapToResponse(saved, null);
    }

    @Transactional(readOnly = true)
    public List<SysDictResponse> getAll(String locale) {
        String effective = effectiveLocale(locale);
        return sysDictRepository.findAll().stream()
                .map(d -> mapToResponse(d, effective))
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách SysDict có phân trang, lọc theo query (dictType) và loại trừ bản ghi đã xóa.
     * Gọi Repository.getDetails(query, delFlag, pageable). Lọc theo dictType; description nằm ở bảng translation nên không dùng trong query.
     *
     * @param query    điều kiện tìm kiếm (dictType; null = không lọc)
     * @param pageable tham số phân trang (page, size, sort)
     * @return Page chứa danh sách SysDictResponse và thông tin phân trang
     */
    @Transactional(readOnly = true)
    public Page<SysDictResponse> getPage(SysDictQueryDto query, Pageable pageable, String locale) {
        String effective = effectiveLocale(locale);
        SysDictQueryDto safeQuery = query != null ? query : new SysDictQueryDto();
        Pageable safePageable = sanitizePageableSort(pageable);
        return sysDictRepository.getDetails(safeQuery, DelFlagConstants.DELETED, safePageable)
                .map(d -> mapToResponse(d, effective));
    }

    /**
     * Chỉ giữ lại sort theo các trường tồn tại trên SysDict. Trường "string" map sang "dictType".
     * Tránh lỗi UnknownPathException khi client gửi sort=string hoặc trường không tồn tại.
     */
    private Pageable sanitizePageableSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return pageable;
        }
        Sort safeSort = Sort.by(
                StreamSupport.stream(pageable.getSort().spliterator(), false)
                        .map(order -> {
                            String property = order.getProperty();
                            if ("string".equalsIgnoreCase(property)) {
                                property = "dictType";
                            }
                            return ALLOWED_SORT_FIELDS.contains(property)
                                    ? new Sort.Order(order.getDirection(), property)
                                    : null;
                        })
                        .filter(o -> o != null)
                        .toList()
        );
        if (safeSort.isUnsorted()) {
            safeSort = Sort.by(SORT_DEFAULT_DIRECTION, SORT_DEFAULT);
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), safeSort);
    }

    @Transactional(readOnly = true)
    public SysDictResponse getById(Long id, String locale) {
        SysDict dict = sysDictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
        return mapToResponse(dict, effectiveLocale(locale));
    }

    @Transactional
    public SysDictResponse update(Long id, SysDictRequest request) {
        SysDict dict = sysDictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));

        if (!dict.getDictType().equals(request.getDictType())
                && sysDictRepository.existsByDictType(request.getDictType())) {
            throw new BadRequestException("Dictionary type already exists");
        }

        if (request.getTranslations() == null || request.getTranslations().isEmpty()) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần gửi ít nhất một bản ghi trong translations (locale, description).");
        }
        boolean hasValidTranslation = request.getTranslations().stream()
                .anyMatch(t -> t != null && t.getLocale() != null && !t.getLocale().isBlank());
        if (!hasValidTranslation) {
            throw new BadRequestException("Thiếu dữ liệu đa ngôn ngữ. Cần ít nhất một bản ghi có locale hợp lệ trong translations.");
        }

        dict.setDictType(request.getDictType());

        SysDict updated = sysDictRepository.save(dict);

        // Cập nhật từng bản dịch gửi trong body
        if (request.getTranslations() != null) {
            for (DictLocaleDto t : request.getTranslations()) {
                if (t == null || t.getLocale() == null || t.getLocale().isBlank()) continue;
                SysDictTranslationRequest tr = new SysDictTranslationRequest();
                tr.setLocale(t.getLocale().trim());
                tr.setDescription(t.getDescription());
                saveTranslation(id, tr);
            }
        }

        return mapToResponse(updated, null);
    }

    @Transactional
    public void delete(Long id) {
        SysDict dict = sysDictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", id));
        sysDictTranslationRepository.deleteByDictId(id);
        sysDictRepository.delete(dict);
    }

    /** Lưu hoặc cập nhật bản dịch theo locale cho một dictionary. */
    @Transactional
    public void saveTranslation(Long dictId, SysDictTranslationRequest request) {
        sysDictRepository.findById(dictId)
                .orElseThrow(() -> new ResourceNotFoundException("Dictionary", "id", dictId));
        String locale = request.getLocale().trim();
        SysDictTranslation t = sysDictTranslationRepository.findByDictIdAndLocale(dictId, locale)
                .orElse(SysDictTranslation.builder().dictId(dictId).locale(locale).build());
        t.setDescription(request.getDescription());
        sysDictTranslationRepository.save(t);
    }

    /** Trả về locale hợp lệ: nếu null/blank thì dùng DEFAULT_LOCALE (vi). */
    private String effectiveLocale(String locale) {
        return (locale != null && !locale.isBlank()) ? locale.trim() : DEFAULT_LOCALE;
    }

    private SysDictResponse mapToResponse(SysDict dict, String locale) {
        String description = null;
        if (locale != null && !locale.isBlank()) {
            description = sysDictTranslationRepository.findByDictIdAndLocale(dict.getId(), locale.trim())
                    .filter(t -> t.getDescription() != null && !t.getDescription().isBlank())
                    .map(SysDictTranslation::getDescription)
                    .orElse(null);
        }
        return new SysDictResponse(
                dict.getId(),
                dict.getCreatedAt(),
                dict.getUpdatedAt(),
                dict.getDictType(),
                description
        );
    }
}
