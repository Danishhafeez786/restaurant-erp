package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.PaymentMethodHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.PaymentMethodModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.PaymentMethodSearchCriteria;
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
@RequestMapping("/api/payment-method")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodHandler handler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentMethodModel>> create(@RequestBody PaymentMethodModel model) {

        PaymentMethodModel response = handler.create(model);
        sendEvent("payment-method-created", response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<PaymentMethodModel>builder()
                                .success(true)
                                .message("Payment Method Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<PaymentMethodModel>>> search(
            @RequestBody PaymentMethodSearchCriteria criteria,
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
                ApiResponse.<PageResponse<PaymentMethodModel>>builder()
                        .success(true)
                        .message("Payment Methods fetched successfully")
                        .data(
                                handler.getAll(
                                        criteria,
                                        pageable))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodModel>> update(
            @PathVariable UUID id,
            @RequestBody PaymentMethodModel model) {

        PaymentMethodModel response = handler.update(id, model);
        sendEvent("payment-method-updated", response);
        return ResponseEntity.ok(
                ApiResponse.<PaymentMethodModel>builder()
                        .success(true)
                        .message("Payment Method Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        PaymentMethodModel response = handler.delete(id);
        sendEvent("payment-method-deleted", response);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Payment Method Deleted Successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        PaymentMethodModel response = handler.restore(id);
        sendEvent("payment-method-restored", response);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Payment Method Restored Successfully")
                        .build()
        );
    }

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