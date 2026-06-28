package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrganizationHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrganizationSearchCriteria;
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
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationHandler organizationHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationModel>> create(@RequestBody OrganizationModel model) {

        OrganizationModel response = organizationHandler.create(model);

        sendEvent("organization-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrganizationModel>builder()
                        .success(true)
                        .message("Organization Created Successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrganizationModel>>> search(
            @RequestBody OrganizationSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()),
                        sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<OrganizationModel>>builder()
                        .success(true)
                        .message("Organizations fetched successfully")
                        .data(organizationHandler.getAll(criteria, pageable))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationModel>> update(
            @PathVariable UUID id,
            @RequestBody OrganizationModel model) {

        OrganizationModel response =
                organizationHandler.update(id, model);

        sendEvent("organization-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<OrganizationModel>builder()
                        .success(true)
                        .message("Organization Updated Successfully")
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        OrganizationModel deleted =
                organizationHandler.delete(id);

        sendEvent("organization-deleted", deleted);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Organization Deleted Successfully")
                        .build());
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        OrganizationModel restored =
                organizationHandler.restore(id);

        sendEvent("organization-restored", restored);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Organization Restored Successfully")
                        .build());
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