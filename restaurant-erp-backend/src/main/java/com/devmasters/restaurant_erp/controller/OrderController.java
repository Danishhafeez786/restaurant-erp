package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderSearchCriteria;
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
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderHandler orderHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderModel>> create(@Valid @RequestBody OrderModel model) {

        OrderModel response = orderHandler.create(model);

        sendEvent("order-created", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<OrderModel>builder().success(true).message("Order Created Successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderModel>>> search(@RequestBody OrderSearchCriteria criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy));

        return ResponseEntity.ok(ApiResponse.<PageResponse<OrderModel>>builder().success(true).message("Orders fetched successfully").data(orderHandler.getAll(criteria, pageable)).build());
    }


    @PreAuthorize("hasAuthority('ORDER_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderModel>> getById(@PathVariable UUID id) {

        OrderModel response = orderHandler.getById(id);

        return ResponseEntity.ok(ApiResponse.<OrderModel>builder().success(true).message("Order fetched successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderModel>> update(@PathVariable UUID id, @Valid @RequestBody OrderModel model) {

        OrderModel response = orderHandler.update(id, model);

        sendEvent("order-updated", response);

        return ResponseEntity.ok(ApiResponse.<OrderModel>builder().success(true).message("Order Updated Successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_CONFIRM')")
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderModel>> confirm(@PathVariable UUID id) {

        OrderModel response = orderHandler.confirm(id);

        sendEvent("order-confirmed", response);

        return ResponseEntity.ok(ApiResponse.<OrderModel>builder().success(true).message("Order Confirmed Successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_PREPARE')")
    @PatchMapping("/{id}/preparing")
    public ResponseEntity<ApiResponse<OrderModel>> startPreparing(@PathVariable UUID id) {

        OrderModel response = orderHandler.startPreparing(id);

        sendEvent("order-preparing", response);

        return ResponseEntity.ok(ApiResponse.<OrderModel>builder().success(true).message("Order Preparation Started Successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_READY')")
    @PatchMapping("/{id}/ready")
    public ResponseEntity<ApiResponse<OrderModel>> markReady(@PathVariable UUID id) {

        OrderModel response = orderHandler.markReady(id);

        sendEvent("order-ready", response);

        return ResponseEntity.ok(ApiResponse.<OrderModel>builder().success(true).message("Order Marked Ready Successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_COMPLETE')")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<OrderModel>> complete(@PathVariable UUID id) {

        OrderModel response = orderHandler.complete(id);

        sendEvent("order-completed", response);

        return ResponseEntity.ok(ApiResponse.<OrderModel>builder().success(true).message("Order Completed Successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_CANCEL')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderModel>> cancel(@PathVariable UUID id, @RequestParam String cancellationReason) {

        OrderModel response = orderHandler.cancel(id, cancellationReason);

        sendEvent("order-cancelled", response);

        return ResponseEntity.ok(ApiResponse.<OrderModel>builder().success(true).message("Order Cancelled Successfully").data(response).build());
    }


    @PreAuthorize("hasAuthority('ORDER_VIEW')")
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