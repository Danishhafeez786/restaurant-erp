package com.devmasters.restaurant_erp.paymentmethod.controller;

import com.devmasters.restaurant_erp.paymentmethod.handler.PaymentMethodHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.paymentmethod.model.PaymentMethodModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.paymentmethod.model.searchCriteria.PaymentMethodSearchCriteria;
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
@RequestMapping("/api/payment-method")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodHandler handler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('PAYMENT_METHOD_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentMethodModel>> create(
            @Valid @RequestBody PaymentMethodModel model) {

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


    @PreAuthorize("hasAuthority('PAYMENT_METHOD_VIEW')")
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
                        sortBy
                )
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<PaymentMethodModel>>builder()
                        .success(true)
                        .message("Payment Methods fetched successfully")
                        .data(handler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('PAYMENT_METHOD_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentMethodModel model) {

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


    @PreAuthorize("hasAuthority('PAYMENT_METHOD_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        PaymentMethodModel response = handler.delete(id);
        sendEvent("payment-method-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Payment Method Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('PAYMENT_METHOD_RESTORE')")
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


    @PreAuthorize("hasAuthority('PAYMENT_METHOD_VIEW')")
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