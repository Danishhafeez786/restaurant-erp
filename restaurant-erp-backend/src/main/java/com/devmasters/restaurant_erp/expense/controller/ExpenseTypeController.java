package com.devmasters.restaurant_erp.expense.controller;

import com.devmasters.restaurant_erp.expense.handler.ExpenseTypeHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.expense.model.ExpenseTypeModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseTypeSearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/expense-type")
@RequiredArgsConstructor
public class ExpenseTypeController {

    private final ExpenseTypeHandler handler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('EXPENSE_TYPE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseTypeModel>> create(
            @Valid @RequestBody ExpenseTypeModel model) {

        ExpenseTypeModel response = handler.create(model);
        sendEvent("expense-type-created", response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ExpenseTypeModel>builder()
                                .success(true)
                                .message("Expense Type Created Successfully")
                                .data(response)
                                .build()
                );
    }


    @PreAuthorize("hasAuthority('EXPENSE_TYPE_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseTypeModel>>> search(
            @RequestBody ExpenseTypeSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()),
                        sortBy
                )
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ExpenseTypeModel>>builder()
                        .success(true)
                        .message("Expense Types fetched successfully")
                        .data(handler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_TYPE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseTypeModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ExpenseTypeModel model) {

        ExpenseTypeModel response = handler.update(id, model);
        sendEvent("expense-type-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<ExpenseTypeModel>builder()
                        .success(true)
                        .message("Expense Type Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_TYPE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        ExpenseTypeModel response = handler.delete(id);
        sendEvent("expense-type-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Type Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_TYPE_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        ExpenseTypeModel response = handler.restore(id);
        sendEvent("expense-type-restored", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Type Restored Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('EXPENSE_TYPE_VIEW')")
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