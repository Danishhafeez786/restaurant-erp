package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.CategoryHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.Menu.CategoryModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.CategorySearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryHandler categoryHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryModel>> create(
            @Valid @RequestBody CategoryModel model) {

        CategoryModel response = categoryHandler.create(model);
        sendEvent("category-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CategoryModel>builder()
                        .success(true)
                        .message("Category Created Successfully")
                        .data(response)
                        .build()
                );
    }


    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<CategoryModel>>> search(
            @RequestBody CategorySearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()), sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<CategoryModel>>builder()
                        .success(true)
                        .message("Categories fetched successfully")
                        .data(categoryHandler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryModel>> update(@PathVariable UUID id, @Valid @RequestBody CategoryModel model) {
        CategoryModel response = categoryHandler.update(id, model);
        sendEvent("category-updated", response);
        return ResponseEntity.ok(
                ApiResponse.<CategoryModel>builder()
                        .success(true)
                        .message("Category Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        CategoryModel deleted = categoryHandler.delete(id);
        sendEvent("category-deleted", deleted);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('CATEGORY_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {
        CategoryModel restored = categoryHandler.restore(id);
        sendEvent("category-restored", restored);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category Restored Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        return emitter;
    }


    private void sendEvent(String eventName, Object data) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name(eventName)
                                .data(data)
                );
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        });
    }
}