package com.devmasters.restaurant_erp.rolepermission.controller;

import com.devmasters.restaurant_erp.rolepermission.handler.RolePermissionHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.rolepermission.model.RolePermissionModel;
import com.devmasters.restaurant_erp.rolepermission.model.searchCriteria.RolePermissionSearchCriteria;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.rolepermission.model.settings.PermissionMatrixResponse;
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
@RequestMapping("/api/role-permission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionHandler rolePermissionHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('ROLE_PERMISSION_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RolePermissionModel>> create(
            @Valid @RequestBody RolePermissionModel model){

        RolePermissionModel response = rolePermissionHandler.create(model);
        sendEvent("role-permission-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RolePermissionModel>builder()
                        .success(true)
                        .message("Role Permission Created Successfully")
                        .data(response)
                        .build()
                );
    }


    @PreAuthorize("hasAuthority('ROLE_PERMISSION_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<RolePermissionModel>>> search(
            @RequestBody RolePermissionSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<RolePermissionModel>>builder()
                        .success(true)
                        .message("Role Permissions fetched successfully")
                        .data(rolePermissionHandler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('ROLE_PERMISSION_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RolePermissionModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody RolePermissionModel model) {

        RolePermissionModel response = rolePermissionHandler.update(id, model);
        sendEvent("role-permission-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<RolePermissionModel>builder()
                        .success(true)
                        .message("Role Permission Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('ROLE_PERMISSION_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        RolePermissionModel deleted = rolePermissionHandler.delete(id);
        sendEvent("role-permission-deleted", deleted);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Role Permission Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('ROLE_PERMISSION_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        RolePermissionModel restored = rolePermissionHandler.restore(id);
        sendEvent("role-permission-restored", restored);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Role Permission Restored Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('PERMISSION_MATRIX_VIEW')")
    @GetMapping("/matrix")
    public ResponseEntity<ApiResponse<PermissionMatrixResponse>> matrix() {

        return ResponseEntity.ok(
                ApiResponse.<PermissionMatrixResponse>builder()
                        .success(true)
                        .message("Permission Matrix Loaded Successfully")
                        .data(rolePermissionHandler.getMatrix())
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('ROLE_PERMISSION_VIEW')")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(error -> emitters.remove(emitter));

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
            } catch (IOException ex) {
                emitter.completeWithError(ex);
                emitters.remove(emitter);
            }
        });
    }
}