package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.PermissionHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.PermissionModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.PermissionSearchCriteria;
import jakarta.validation.Valid;
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
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionHandler permissionHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<List<PermissionModel>>> create(
            @Valid @RequestBody PermissionModel model) {

        List<PermissionModel> response =
                permissionHandler.create(model);

        sendEvent("permission-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<List<PermissionModel>>builder()
                        .success(true)
                        .message("Permissions Created Successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<PermissionModel>>> search(
            @RequestBody PermissionSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<PermissionModel>>builder()
                        .success(true)
                        .message("Permissions fetched successfully")
                        .data(permissionHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionModel>> update(
            @Valid  @PathVariable UUID id,
            @RequestBody PermissionModel model) {

        PermissionModel response =
                permissionHandler.update(id, model);

        sendEvent("permission-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<PermissionModel>builder()
                        .success(true)
                        .message("Permission Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        PermissionModel deleted =
                permissionHandler.delete(id);

        sendEvent("permission-deleted", deleted);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Permission Deleted Successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        PermissionModel restored =
                permissionHandler.restore(id);

        sendEvent("permission-restored", restored);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Permission Restored Successfully")
                        .build()
        );
    }

    @GetMapping(
            value = "/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {

        SseEmitter emitter =
                new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(() ->
                emitters.remove(emitter));

        emitter.onTimeout(() ->
                emitters.remove(emitter));

        emitter.onError(e ->
                emitters.remove(emitter));

        return emitter;
    }

    private void sendEvent(
            String eventName,
            Object data) {

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