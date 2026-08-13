package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderStatusHistoryHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderStatusHistoryModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderStatusHistorySearchCriteria;
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
@RequestMapping("/api/order-status-history")
@RequiredArgsConstructor
public class OrderStatusHistoryController {

    private final OrderStatusHistoryHandler orderStatusHistoryHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('ORDER_STATUS_HISTORY_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderStatusHistoryModel>> create(
            @Valid @RequestBody OrderStatusHistoryModel model) {

        OrderStatusHistoryModel response = orderStatusHistoryHandler.create(model);
        sendEvent("order-status-history-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrderStatusHistoryModel>builder()
                        .success(true)
                        .message("Order Status History Created Successfully")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAuthority('ORDER_STATUS_HISTORY_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderStatusHistoryModel>>> search(
            @RequestBody OrderStatusHistorySearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "changedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy)
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<OrderStatusHistoryModel>>builder()
                        .success(true)
                        .message("Order Status History Fetched Successfully")
                        .data(orderStatusHistoryHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_STATUS_HISTORY_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderStatusHistoryModel>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderStatusHistoryModel>builder()
                        .success(true)
                        .message("Order Status History Fetched Successfully")
                        .data(orderStatusHistoryHandler.getById(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_STATUS_HISTORY_VIEW')")
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