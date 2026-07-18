package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.RestaurantTableHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.RestaurantTableModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.RestaurantTableSearchCriteria;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/table-management")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableHandler restaurantTableHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('TABLE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RestaurantTableModel>> create(@Valid @RequestBody RestaurantTableModel model) {

        RestaurantTableModel response = restaurantTableHandler.create(model);
        sendEvent("table-created", response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<RestaurantTableModel>builder()
                                .success(true)
                                .message("Table Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PreAuthorize("hasAuthority('TABLE_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<RestaurantTableModel>>> search(
            @RequestBody RestaurantTableSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()),
                        sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<RestaurantTableModel>>builder()
                        .success(true)
                        .message("Tables fetched successfully")
                        .data(
                                restaurantTableHandler.getAll(
                                        criteria,
                                        pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TABLE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantTableModel>> update(@PathVariable UUID id,@Valid @RequestBody RestaurantTableModel model) {

        RestaurantTableModel response = restaurantTableHandler.update(id, model);
        sendEvent("table-updated", response);
        return ResponseEntity.ok(
                ApiResponse.<RestaurantTableModel>builder()
                        .success(true)
                        .message("Table Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TABLE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        RestaurantTableModel deleted = restaurantTableHandler.delete(id);
        sendEvent("table-deleted", deleted);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Table Deleted Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TABLE_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {

        RestaurantTableModel restored = restaurantTableHandler.restore(id);
        sendEvent("table-restored", restored);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Table Restored Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TABLE_VIEW')")
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
                                .data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        });
    }
}