package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.FloorHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.FloorModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.FloorSearchCriteria;
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
@RequestMapping("/api/floor")
@RequiredArgsConstructor
public class FloorController {

    private final FloorHandler floorHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('FLOOR_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<FloorModel>> create(
            @Valid @RequestBody FloorModel model) {

        FloorModel response = floorHandler.create(model);
        sendEvent("floor-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<FloorModel>builder()
                                .success(true)
                                .message("Floor Created Successfully")
                                .data(response)
                                .build()
                );
    }


    @PreAuthorize("hasAuthority('FLOOR_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<FloorModel>>> search(
            @RequestBody FloorSearchCriteria criteria,
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
                ));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<FloorModel>>builder()
                        .success(true)
                        .message("Floors fetched successfully")
                        .data(floorHandler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('FLOOR_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FloorModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody FloorModel model) {

        FloorModel response = floorHandler.update(id, model);
        sendEvent("floor-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<FloorModel>builder()
                        .success(true)
                        .message("Floor Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('FLOOR_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        FloorModel deleted = floorHandler.delete(id);
        sendEvent("floor-deleted", deleted);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Floor Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('FLOOR_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        FloorModel restored = floorHandler.restore(id);
        sendEvent("floor-restored", restored);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Floor Restored Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('FLOOR_VIEW')")
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

            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        });
    }
}