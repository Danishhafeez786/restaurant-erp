package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.SubscriptionPlanHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.SubscriptionModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.SubscriptionPlanSearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription_plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanHandler subscriptionPlanHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionModel>> create(
            @Valid @RequestBody SubscriptionModel model) {

        SubscriptionModel response = subscriptionPlanHandler.create(model);

        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("subscription-created")
                                .data(response)
                );
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SubscriptionModel>builder()
                        .success(true)
                        .message("Subscription Plan Created Successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<SubscriptionModel>>> search(
            @RequestBody SubscriptionPlanSearchCriteria criteria,
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

        PageResponse<SubscriptionModel> response =
                subscriptionPlanHandler.getAll(criteria, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<SubscriptionModel>>builder()
                        .success(true)
                        .message("Subscription Plans fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriptionModel>> update(
            @Valid  @PathVariable UUID id,
            @RequestBody SubscriptionModel model) {

        SubscriptionModel response =
                subscriptionPlanHandler.update(id, model);

        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("subscription-updated")
                                .data(response)
                );
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(
                ApiResponse.<SubscriptionModel>builder()
                        .success(true)
                        .message("Subscription Plan Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        subscriptionPlanHandler.delete(id);

        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("subscription-deleted")
                                .data(id)
                );
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Subscription Plan Deleted Successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        subscriptionPlanHandler.restore(id);

        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("subscription-restored")
                                .data(id)
                );
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Subscription Plan Restored Successfully")
                        .build()
        );
    }

    @GetMapping(
            value = "/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter stream() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }
}