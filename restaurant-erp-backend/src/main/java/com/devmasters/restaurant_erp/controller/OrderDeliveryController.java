package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderDeliveryHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderDeliveryModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDeliverySearchCriteria;
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
@RequestMapping("/api/order-delivery")
@RequiredArgsConstructor
public class OrderDeliveryController {

    private final OrderDeliveryHandler orderDeliveryHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('ORDER_DELIVERY_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDeliveryModel>> create(@Valid @RequestBody OrderDeliveryModel model) {
        OrderDeliveryModel response = orderDeliveryHandler.create(model);
        sendEvent("order-delivery-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrderDeliveryModel>builder()
                        .success(true)
                        .message("Order Delivery Created Successfully")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAuthority('ORDER_DELIVERY_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderDeliveryModel>>> search(
            @RequestBody OrderDeliverySearchCriteria criteria,
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
                ApiResponse.<PageResponse<OrderDeliveryModel>>builder()
                        .success(true)
                        .message("Order Deliveries Fetched Successfully")
                        .data(orderDeliveryHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DELIVERY_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDeliveryModel>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.<OrderDeliveryModel>builder()
                        .success(true)
                        .message("Order Delivery Fetched Successfully")
                        .data(orderDeliveryHandler.getById(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DELIVERY_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDeliveryModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrderDeliveryModel model) {

        OrderDeliveryModel response = orderDeliveryHandler.update(id, model);
        sendEvent("order-delivery-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<OrderDeliveryModel>builder()
                        .success(true)
                        .message("Order Delivery Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DELIVERY_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        OrderDeliveryModel response = orderDeliveryHandler.delete(id);
        sendEvent("order-delivery-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Delivery Deleted Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DELIVERY_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {
        OrderDeliveryModel response = orderDeliveryHandler.restore(id);
        sendEvent("order-delivery-restored", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Delivery Restored Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_DELIVERY_VIEW')")
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