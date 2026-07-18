package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.ExpenseAttachmentHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.Expense.ExpenseAttachmentModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseAttachmentSearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/expense-attachment")
@RequiredArgsConstructor
public class ExpenseAttachmentController {

    private final ExpenseAttachmentHandler handler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseAttachmentModel>> create(
            @Valid @RequestBody ExpenseAttachmentModel model) {

        ExpenseAttachmentModel response = handler.create(model);
        sendEvent(
                "expense-attachment-created",
                response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ExpenseAttachmentModel>builder()
                                .success(true)
                                .message("Expense Attachment Created Successfully")
                                .data(response)
                                .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ExpenseAttachmentModel>>> search(
            @RequestBody ExpenseAttachmentSearchCriteria criteria,
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
                ApiResponse.<PageResponse<ExpenseAttachmentModel>>builder()
                        .success(true)
                        .message("Expense Attachments fetched successfully")
                        .data(
                                handler.getAll(
                                        criteria,
                                        pageable))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseAttachmentModel>> update(
            @Valid @PathVariable UUID id,
            @RequestBody ExpenseAttachmentModel model) {
        ExpenseAttachmentModel response =
                handler.update(
                        id,
                        model);
        sendEvent(
                "expense-attachment-updated",
                response);
        return ResponseEntity.ok(
                ApiResponse.<ExpenseAttachmentModel>builder()
                        .success(true)
                        .message("Expense Attachment Updated Successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {
        handler.delete(id);
        sendEvent(
                "expense-attachment-deleted",
                id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Attachment Deleted Successfully")
                        .build());
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        handler.restore(id);
        sendEvent(
                "expense-attachment-restored",
                id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Attachment Restored Successfully")
                        .build());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(e));
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