package com.devmasters.restaurant_erp.expense.controller;

import com.devmasters.restaurant_erp.expense.handler.ExpenseHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.expense.model.ExpenseModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseSearchCriteria;
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
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseHandler handler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('EXPENSE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseModel>> create(
            @Valid @RequestBody ExpenseModel model) {

        ExpenseModel response = handler.create(model);
        sendEvent("expense-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ExpenseModel>builder()
                                .success(true)
                                .message("Expense Created Successfully")
                                .data(response)
                                .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseModel>>> search(
            @RequestBody ExpenseSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.valueOf(
                                        direction.toUpperCase()),
                                sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ExpenseModel>>builder()
                        .success(true)
                        .message("Expenses fetched successfully")
                        .data(handler.getAll(criteria, pageable))
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ExpenseModel model) {

        ExpenseModel response =
                handler.update(id, model);

        sendEvent("expense-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<ExpenseModel>builder()
                        .success(true)
                        .message("Expense Updated Successfully")
                        .data(response)
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        ExpenseModel response = handler.delete(id);

        sendEvent("expense-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Deleted Successfully")
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        ExpenseModel response = handler.restore(id);

        sendEvent(
                "expense-restored",
                response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Restored Successfully")
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_VIEW')")
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
                                .data(data));

            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        });
    }
}