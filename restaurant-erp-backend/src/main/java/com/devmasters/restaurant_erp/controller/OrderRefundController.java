package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderRefundHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderRefundModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderRefundSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order-refund")
@RequiredArgsConstructor
public class OrderRefundController {

    private final OrderRefundHandler orderRefundHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderRefundModel>> create(
            @RequestBody OrderRefundModel model) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OrderRefundModel>builder()
                        .success(true)
                        .message("Order Refund Created Successfully")
                        .data(orderRefundHandler.create(model))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderRefundModel>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderRefundModel>builder()
                        .success(true)
                        .message("Order Refund Fetched Successfully")
                        .data(orderRefundHandler.getById(id))
                        .build()
        );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderRefundModel>>> search(
            @RequestBody OrderRefundSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.fromString(direction),
                        sortBy
                )
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<OrderRefundModel>>builder()
                        .success(true)
                        .message("Order Refunds Fetched Successfully")
                        .data(orderRefundHandler.search(criteria, pageable))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderRefundModel>> update(
            @PathVariable UUID id,
            @RequestBody OrderRefundModel model) {

        return ResponseEntity.ok(
                ApiResponse.<OrderRefundModel>builder()
                        .success(true)
                        .message("Order Refund Updated Successfully")
                        .data(orderRefundHandler.update(id, model))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderRefundModel>> delete(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderRefundModel>builder()
                        .success(true)
                        .message("Order Refund Deleted Successfully")
                        .data(orderRefundHandler.delete(id))
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<OrderRefundModel>> restore(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderRefundModel>builder()
                        .success(true)
                        .message("Order Refund Restored Successfully")
                        .data(orderRefundHandler.restore(id))
                        .build()
        );
    }

    @PatchMapping("/{id}/process")
    public ResponseEntity<ApiResponse<OrderRefundModel>> processRefund(
            @PathVariable UUID id,
            @RequestParam UUID processedById) {

        return ResponseEntity.ok(
                ApiResponse.<OrderRefundModel>builder()
                        .success(true)
                        .message("Refund Processed Successfully")
                        .data(
                                orderRefundHandler.processRefund(
                                        id,
                                        processedById
                                )
                        )
                        .build()
        );
    }
}