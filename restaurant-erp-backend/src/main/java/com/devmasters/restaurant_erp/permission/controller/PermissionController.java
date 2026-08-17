package com.devmasters.restaurant_erp.permission.controller;

import com.devmasters.restaurant_erp.permission.handler.PermissionHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.permission.model.PermissionModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.permission.model.searchCriteria.PermissionSearchCriteria;
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
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionHandler permissionHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
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

    @PreAuthorize("hasAuthority('PERMISSION_VIEW')")
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

    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody PermissionModel model) {

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

    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
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
    @PreAuthorize("hasAuthority('PERMISSION_RESTORE')")
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
    @PreAuthorize("hasAuthority('PERMISSION_VIEW')")
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