package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.SubscriptionPlanHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.SubscriptionModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription_plans")
public class SubscriptionPlanController {
    private final SubscriptionPlanHandler subscriptionPlanHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionModel>> create(@RequestBody SubscriptionModel model) {

        SubscriptionModel response = subscriptionPlanHandler.create(model);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SubscriptionModel>builder()
                                .success(true)
                                .message("Subscription Plan Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<SubscriptionModel>>> search(
            @RequestBody SubscriptionPlanSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy));

        PageResponse<SubscriptionModel> response = subscriptionPlanHandler.getAll(criteria, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<SubscriptionModel>>builder()
                        .success(true)
                        .message("Subscription Plans fetched successfully")
                        .data(response)
                        .build()
        );
    }
}
