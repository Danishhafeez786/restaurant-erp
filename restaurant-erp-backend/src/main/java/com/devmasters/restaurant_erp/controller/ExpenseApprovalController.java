package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.ExpenseApprovalHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.Expense.ExpenseApprovalModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseApprovalSearchCriteria;
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
@RequestMapping("/api/expense-approval")
@RequiredArgsConstructor
public class ExpenseApprovalController {

    private final ExpenseApprovalHandler handler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('EXPENSE_APPROVAL_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseApprovalModel>> create(
            @Valid @RequestBody ExpenseApprovalModel model) {

        ExpenseApprovalModel response = handler.create(model);
        sendEvent("expense-approval-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ExpenseApprovalModel>builder()
                                .success(true)
                                .message("Expense Approval Created Successfully")
                                .data(response)
                                .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_APPROVAL_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseApprovalModel>>> search(
            @RequestBody ExpenseApprovalSearchCriteria criteria,
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
                ApiResponse.<PageResponse<ExpenseApprovalModel>>builder()
                        .success(true)
                        .message("Expense Approvals fetched successfully")
                        .data(handler.getAll(criteria, pageable))
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_APPROVAL_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseApprovalModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ExpenseApprovalModel model) {


        ExpenseApprovalModel response = handler.update(id, model);
        sendEvent("expense-approval-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<ExpenseApprovalModel>builder()
                        .success(true)
                        .message("Expense Approval Updated Successfully")
                        .data(response)
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_APPROVAL_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        handler.delete(id);
        sendEvent("expense-approval-deleted", id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Approval Deleted Successfully")
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_APPROVAL_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        handler.restore(id);
        sendEvent("expense-approval-restored", id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Approval Restored Successfully")
                        .build());
    }


    @PreAuthorize("hasAuthority('EXPENSE_APPROVAL_VIEW')")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }


    private void sendEvent(
            String eventName,
            Object data) {
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