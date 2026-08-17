package com.devmasters.restaurant_erp.expense.controller;


import com.devmasters.restaurant_erp.expense.handler.ExpenseCategoryHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.expense.model.ExpenseCategoryModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseCategorySearchCriteria;
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
@RequestMapping("/api/expense-category")
@RequiredArgsConstructor
public class ExpenseCategoryController {

    private final ExpenseCategoryHandler handler;

    private final List<SseEmitter> emitters =
            new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseCategoryModel>> create(
            @Valid @RequestBody ExpenseCategoryModel model) {

        ExpenseCategoryModel response = handler.create(model);
        sendEvent("expense-category-created", response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ExpenseCategoryModel>builder()
                                .success(true)
                                .message("Expense Category Created Successfully")
                                .data(response)
                                .build()
                );
    }


    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseCategoryModel>>> search(
            @RequestBody ExpenseCategorySearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()),
                        sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ExpenseCategoryModel>>builder()
                        .success(true)
                        .message("Expense Categories fetched successfully")
                        .data(handler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseCategoryModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ExpenseCategoryModel model) {

        ExpenseCategoryModel response = handler.update(id, model);
        sendEvent("expense-category-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<ExpenseCategoryModel>builder()
                        .success(true)
                        .message("Expense Category Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        ExpenseCategoryModel response = handler.delete(id);
        sendEvent("expense-category-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Category Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        ExpenseCategoryModel response = handler.restore(id);
        sendEvent("expense-category-restored", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Category Restored Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_CATEGORY_VIEW')")
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

            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        });
    }
}