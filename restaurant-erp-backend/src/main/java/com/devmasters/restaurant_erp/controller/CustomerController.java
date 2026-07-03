package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.CustomerHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.CustomerModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.CustomerSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerHandler customerHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerModel>> create(@RequestBody CustomerModel model) {

        CustomerModel response = customerHandler.create(model);

        sendEvent("customer-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CustomerModel>builder()
                                .success(true)
                                .message("Customer Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<CustomerModel>>> search(
            @RequestBody CustomerSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy));
        PageResponse<CustomerModel> customerModels = customerHandler.getAll(criteria, pageable);

        return ResponseEntity.ok(ApiResponse.<PageResponse<CustomerModel>>builder()
                        .success(true)
                        .message("Customers fetched successfully")
                        .data(customerModels)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerModel>> update(
            @PathVariable UUID id, @RequestBody CustomerModel model) {

        CustomerModel response = customerHandler.update(id, model);

        sendEvent("customer-updated", response);

        return ResponseEntity.ok(ApiResponse.<CustomerModel>builder()
                        .success(true)
                        .message("Customer Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        CustomerModel deleted = customerHandler.delete(id);

        sendEvent("customer-deleted", deleted);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Customer Deleted Successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {

        CustomerModel restored = customerHandler.restore(id);

        sendEvent("customer-restored", restored);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Customer Restored Successfully")
                        .build()
        );
    }

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
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));

            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        });
    }
}