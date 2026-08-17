package com.devmasters.restaurant_erp.order.controller;

import com.devmasters.restaurant_erp.order.handler.OrderSplitHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.order.model.OrderSplitModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSplitSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order-split")
@RequiredArgsConstructor
public class OrderSplitController {

    private final OrderSplitHandler orderSplitHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderSplitModel>> create(
            @RequestBody OrderSplitModel model) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OrderSplitModel>builder()
                        .success(true)
                        .message("Order Split Created Successfully")
                        .data(orderSplitHandler.create(model))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderSplitModel>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderSplitModel>builder()
                        .success(true)
                        .message("Order Split Fetched Successfully")
                        .data(orderSplitHandler.getById(id))
                        .build()
        );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderSplitModel>>> search(
            @RequestBody OrderSplitSearchCriteria criteria,
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
                ApiResponse.<PageResponse<OrderSplitModel>>builder()
                        .success(true)
                        .message("Order Splits Fetched Successfully")
                        .data(orderSplitHandler.search(
                                criteria,
                                pageable
                        ))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderSplitModel>> update(
            @PathVariable UUID id,
            @RequestBody OrderSplitModel model) {

        return ResponseEntity.ok(
                ApiResponse.<OrderSplitModel>builder()
                        .success(true)
                        .message("Order Split Updated Successfully")
                        .data(orderSplitHandler.update(id, model))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderSplitModel>> delete(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderSplitModel>builder()
                        .success(true)
                        .message("Order Split Deleted Successfully")
                        .data(orderSplitHandler.delete(id))
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<OrderSplitModel>> restore(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderSplitModel>builder()
                        .success(true)
                        .message("Order Split Restored Successfully")
                        .data(orderSplitHandler.restore(id))
                        .build()
        );
    }
}