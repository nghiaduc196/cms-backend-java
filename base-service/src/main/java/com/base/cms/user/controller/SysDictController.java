package com.base.cms.user.controller;

import com.base.cms.common.dto.ApiResponse;
import com.base.cms.user.dto.dict.SysDictQueryDto;
import com.base.cms.user.dto.dict.SysDictRequest;
import com.base.cms.user.dto.dict.SysDictResponse;
import com.base.cms.user.dto.dict.SysDictTranslationRequest;
import com.base.cms.user.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dicts")
@RequiredArgsConstructor
@Tag(name = "Dictionary Management", description = "APIs for managing system dictionaries")
public class SysDictController {

    private final SysDictService sysDictService;

    @Operation(summary = "Create a dictionary", description = "Create a new dictionary with type and description")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Dictionary created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or dictionary type already exists")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<SysDictResponse>> create(@Valid @RequestBody SysDictRequest request) {
        SysDictResponse response = sysDictService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Dictionary created successfully", response));
    }

    @Operation(summary = "Get all dictionaries", description = "Retrieve a list of all dictionaries. Optional locale (vi, en, ...) for translated description.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of dictionaries",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<SysDictResponse>>> getAll(
            @Parameter(description = "Locale for translation (vi, en, ...). Optional.") @RequestParam(required = false) String locale) {
        List<SysDictResponse> list = sysDictService.getAll(locale);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @Operation(summary = "Get dictionaries with pagination and filter", description = "Retrieve a paginated list of dictionaries. Params: description, dictType (optional LIKE filter), page, size, sort")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved paginated list",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<Page<SysDictResponse>>> getPage(
            @Parameter(description = "Filter by description (LIKE)") @RequestParam(required = false) String description,
            @Parameter(description = "Filter by dictionary type (LIKE)") @RequestParam(required = false) String dictType,
            @Parameter(description = "Locale for translation (vi, en, ...). Optional.") @RequestParam(required = false) String locale,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        SysDictQueryDto query = new SysDictQueryDto(description, dictType);
        Page<SysDictResponse> page = sysDictService.getPage(query, pageable, locale);
        return ResponseEntity.ok(ApiResponse.success(page));
    }

    @Operation(summary = "Get dictionary by ID", description = "Retrieve a dictionary by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Dictionary found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Dictionary not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SysDictResponse>> getById(
            @Parameter(description = "Dictionary ID", required = true) @PathVariable Long id,
            @Parameter(description = "Locale for translation (vi, en, ...). Optional.") @RequestParam(required = false) String locale) {
        SysDictResponse response = sysDictService.getById(id, locale);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Update dictionary", description = "Update an existing dictionary")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Dictionary updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Dictionary not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or dictionary type already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SysDictResponse>> update(
            @Parameter(description = "Dictionary ID", required = true) @PathVariable Long id,
            @Valid @RequestBody SysDictRequest request) {
        SysDictResponse response = sysDictService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Dictionary updated successfully", response));
    }

    @Operation(summary = "Delete dictionary", description = "Delete a dictionary by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Dictionary deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Dictionary not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Dictionary ID", required = true) @PathVariable Long id) {
        sysDictService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Dictionary deleted successfully", null));
    }

    @Operation(summary = "Save dictionary translation", description = "Add or update translation for a dictionary by locale (vi, en, ...). Supports multiple languages.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Translation saved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Dictionary not found")
    })
    @PutMapping("/{id}/translations")
    public ResponseEntity<ApiResponse<Void>> saveTranslation(
            @Parameter(description = "Dictionary ID", required = true) @PathVariable Long id,
            @Valid @RequestBody SysDictTranslationRequest request) {
        sysDictService.saveTranslation(id, request);
        return ResponseEntity.ok(ApiResponse.success("Translation saved", null));
    }
}
