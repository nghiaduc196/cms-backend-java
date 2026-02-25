package com.base.cms.user.controller;

import com.base.cms.common.dto.ApiResponse;
import com.base.cms.user.dto.SysDictItemRequest;
import com.base.cms.user.dto.SysDictItemResponse;
import com.base.cms.user.service.SysDictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict-items")
@RequiredArgsConstructor
@Tag(name = "Dictionary Item Management", description = "APIs for managing dictionary items. Requires dictId and active dictionary.")
public class SysDictItemController {

    private final SysDictItemService sysDictItemService;

    @Operation(summary = "Create a dictionary item", description = "Create a new item. dictId is required; dictionary must exist and be active (not deleted, not locked).")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Dictionary item created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, or dictionary is deleted/not active"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Dictionary not found")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<SysDictItemResponse>> create(@Valid @RequestBody SysDictItemRequest request) {
        SysDictItemResponse response = sysDictItemService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Dictionary item created successfully", response));
    }

    @Operation(summary = "Get all dictionary items", description = "Retrieve a list of all dictionary items")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<SysDictItemResponse>>> getAll() {
        List<SysDictItemResponse> list = sysDictItemService.getAll();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @Operation(summary = "Get dictionary items by dict ID", description = "Retrieve items of a dictionary, ordered by sortOrder")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/by-dict/{dictId}")
    public ResponseEntity<ApiResponse<List<SysDictItemResponse>>> getByDictId(
            @Parameter(description = "Dictionary ID", required = true) @PathVariable Long dictId) {
        List<SysDictItemResponse> list = sysDictItemService.getByDictId(dictId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @Operation(summary = "Get dictionary item by ID", description = "Retrieve a dictionary item by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Item found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Dictionary item not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SysDictItemResponse>> getById(
            @Parameter(description = "Dictionary item ID", required = true) @PathVariable Long id) {
        SysDictItemResponse response = sysDictItemService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Update dictionary item", description = "Update an existing dictionary item. dictId must reference an active dictionary.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Dictionary item updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Dictionary item or dictionary not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Dictionary is deleted/not active")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SysDictItemResponse>> update(
            @Parameter(description = "Dictionary item ID", required = true) @PathVariable Long id,
            @Valid @RequestBody SysDictItemRequest request) {
        SysDictItemResponse response = sysDictItemService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Dictionary item updated successfully", response));
    }

    @Operation(summary = "Delete dictionary item", description = "Delete a dictionary item by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Dictionary item deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Dictionary item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "Dictionary item ID", required = true) @PathVariable Long id) {
        sysDictItemService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Dictionary item deleted successfully", null));
    }
}
