package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderDiscountHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderDiscountModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDiscountSearchCriteria;
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
@RequestMapping("/api/order-discount")
@RequiredArgsConstructor
public class OrderDiscountController {

    private final OrderDiscountHandler orderDiscountHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('ORDER_DISCOUNT_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDiscountModel>> create(@Valid @RequestBody OrderDiscountModel model) {
        OrderDiscountModel response = orderDiscountHandler.create(model);
        sendEvent("order-discount-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrderDiscountModel>builder()
                        .success(true)
                        .message("Order Discount Created Successfully")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAuthority('ORDER_DISCOUNT_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderDiscountModel>>> search(
            @RequestBody OrderDiscountSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy)
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<OrderDiscountModel>>builder()
                        .success(true)
                        .message("Order Discounts Fetched Successfully")
                        .data(orderDiscountHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DISCOUNT_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDiscountModel>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.<OrderDiscountModel>builder()
                        .success(true)
                        .message("Order Discount Fetched Successfully")
                        .data(orderDiscountHandler.getById(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DISCOUNT_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDiscountModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrderDiscountModel model) {

        OrderDiscountModel response = orderDiscountHandler.update(id, model);
        sendEvent("order-discount-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<OrderDiscountModel>builder()
                        .success(true)
                        .message("Order Discount Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DISCOUNT_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        OrderDiscountModel response = orderDiscountHandler.delete(id);
        sendEvent("order-discount-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Discount Deleted Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DISCOUNT_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {
        OrderDiscountModel response = orderDiscountHandler.restore(id);
        sendEvent("order-discount-restored", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Discount Restored Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DISCOUNT_VIEW')")
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
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        });
    }
}