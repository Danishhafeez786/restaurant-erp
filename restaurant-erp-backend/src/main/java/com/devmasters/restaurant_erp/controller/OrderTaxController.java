package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderTaxHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderTaxModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderTaxSearchCriteria;
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
@RequestMapping("/api/order-tax")
@RequiredArgsConstructor
public class OrderTaxController {

    private final OrderTaxHandler orderTaxHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('ORDER_TAX_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderTaxModel>> create(
            @Valid @RequestBody OrderTaxModel model) {

        OrderTaxModel response = orderTaxHandler.create(model);
        sendEvent("order-tax-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrderTaxModel>builder()
                        .success(true)
                        .message("Order Tax Created Successfully")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAuthority('ORDER_TAX_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderTaxModel>>> search(
            @RequestBody OrderTaxSearchCriteria criteria,
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
                ApiResponse.<PageResponse<OrderTaxModel>>builder()
                        .success(true)
                        .message("Order Taxes Fetched Successfully")
                        .data(orderTaxHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_TAX_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderTaxModel>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderTaxModel>builder()
                        .success(true)
                        .message("Order Tax Fetched Successfully")
                        .data(orderTaxHandler.getById(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_TAX_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderTaxModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrderTaxModel model) {

        OrderTaxModel response = orderTaxHandler.update(id, model);
        sendEvent("order-tax-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<OrderTaxModel>builder()
                        .success(true)
                        .message("Order Tax Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_TAX_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        OrderTaxModel response = orderTaxHandler.delete(id);
        sendEvent("order-tax-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Tax Deleted Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_TAX_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        OrderTaxModel response = orderTaxHandler.restore(id);
        sendEvent("order-tax-restored", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Order Tax Restored Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ORDER_TAX_VIEW')")
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