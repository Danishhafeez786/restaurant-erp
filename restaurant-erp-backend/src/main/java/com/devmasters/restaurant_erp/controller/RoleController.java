package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.RoleHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.RoleModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.RoleSearchCriteria;
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
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleHandler roleHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<RoleModel>> create(@RequestBody RoleModel model) {

        RoleModel response = roleHandler.create(model);
        sendEvent("role-created", response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleModel>builder()
                                .success(true)
                                .message("Role Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<RoleModel>>> search(
            @RequestBody RoleSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()), sortBy));
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<RoleModel>>builder()
                        .success(true)
                        .message("Roles fetched successfully")
                        .data(roleHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleModel>> update(@PathVariable UUID id,
                                                         @RequestBody RoleModel model) {
        RoleModel response = roleHandler.update(id, model);
        sendEvent("role-updated", response);
        return ResponseEntity.ok(
                ApiResponse.<RoleModel>builder()
                        .success(true)
                        .message("Role Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        RoleModel deleted = roleHandler.delete(id);
        sendEvent("role-deleted", deleted);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Role Deleted Successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {
        RoleModel restored = roleHandler.restore(id);
        sendEvent("role-restored", restored);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Role Restored Successfully")
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