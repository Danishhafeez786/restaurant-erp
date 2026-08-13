package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderAttachmentHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderAttachmentModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderAttachmentSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/order-attachment")
@RequiredArgsConstructor
public class OrderAttachmentController {

    private final OrderAttachmentHandler orderAttachmentHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<OrderAttachmentModel>> create(
            @RequestPart("file") MultipartFile file,
            @RequestPart("attachment") OrderAttachmentModel model) {

        OrderAttachmentModel response = orderAttachmentHandler.create(model, file);
        sendEvent("order-attachment-created", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<OrderAttachmentModel>builder().success(true).message("Order Attachment Created Successfully").data(response).build());
    }

    // =========================================================
    // SEARCH
    // =========================================================

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderAttachmentModel>>> search(
            @RequestBody OrderAttachmentSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(ApiResponse.<PageResponse<OrderAttachmentModel>>builder().success(true).message("Order Attachments Fetched Successfully").data(orderAttachmentHandler.getAll(criteria, pageable)).build());
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderAttachmentModel>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.<OrderAttachmentModel>builder().success(true).message("Order Attachment Fetched Successfully").data(orderAttachmentHandler.getById(id)).build());
    }

    // =========================================================
    // UPDATE METADATA
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderAttachmentModel>> update(
            @PathVariable UUID id,
            @RequestBody OrderAttachmentModel model) {

        OrderAttachmentModel response = orderAttachmentHandler.update(id, model);
        sendEvent("order-attachment-updated", response);
        return ResponseEntity.ok(ApiResponse.<OrderAttachmentModel>builder().success(true).message("Order Attachment Updated Successfully").data(response).build());
    }

    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        OrderAttachmentModel response = orderAttachmentHandler.delete(id);
        sendEvent("order-attachment-deleted", response);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Order Attachment Deleted Successfully").build());
    }

    // =========================================================
    // RESTORE
    // =========================================================

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {

        OrderAttachmentModel response = orderAttachmentHandler.restore(id);
        sendEvent("order-attachment-restored", response);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).message("Order Attachment Restored Successfully").build());
    }

    // =========================================================
    // SSE
    // =========================================================

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(error -> emitters.remove(emitter));
        return emitter;
    }

    // =========================================================
    // SEND EVENT
    // =========================================================

    private void sendEvent(String eventName, Object data) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        });
    }
}