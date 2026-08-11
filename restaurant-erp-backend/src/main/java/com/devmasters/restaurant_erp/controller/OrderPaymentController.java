package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderPaymentHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderPaymentModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderPaymentSearchCriteria;
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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/order-payment")
@RequiredArgsConstructor
public class OrderPaymentController {

    private final OrderPaymentHandler orderPaymentHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('ORDER_PAYMENT_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderPaymentModel>> create(
            @Valid @RequestBody OrderPaymentModel model) {

        OrderPaymentModel response = orderPaymentHandler.create(model);
        sendEvent("order-payment-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrderPaymentModel>builder()
                        .success(true)
                        .message("Order Payment Created Successfully")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAuthority('ORDER_PAYMENT_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderPaymentModel>>> search(
            @RequestBody OrderPaymentSearchCriteria criteria,
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
                ApiResponse.<PageResponse<OrderPaymentModel>>builder()
                        .success(true)
                        .message("Order Payments Fetched Successfully")
                        .data(orderPaymentHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_PAYMENT_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderPaymentModel>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderPaymentModel>builder()
                        .success(true)
                        .message("Order Payment Fetched Successfully")
                        .data(orderPaymentHandler.getById(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_PAYMENT_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderPaymentModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrderPaymentModel model) {

        OrderPaymentModel response =
                orderPaymentHandler.update(id, model);

        sendEvent("order-payment-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<OrderPaymentModel>builder()
                        .success(true)
                        .message("Order Payment Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_PAYMENT_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        orderPaymentHandler.delete(id);
        sendEvent("order-payment-deleted", id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Payment Deleted Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_PAYMENT_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        orderPaymentHandler.restore(id);
        sendEvent("order-payment-restored", id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Payment Restored Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_PAYMENT_VIEW')")
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