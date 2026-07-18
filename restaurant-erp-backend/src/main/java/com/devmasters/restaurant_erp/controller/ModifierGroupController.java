package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.ModifierGroupHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.Menu.ModifierGroupModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierGroupSearchCriteria;
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
@RequestMapping("/api/modifier-group")
@RequiredArgsConstructor
public class ModifierGroupController {


    private final ModifierGroupHandler modifierGroupHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('MODIFIER_GROUP_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ModifierGroupModel>> create(
            @Valid @RequestBody ModifierGroupModel model) {

        ModifierGroupModel response = modifierGroupHandler.create(model);

        sendEvent("modifier-group-created", response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse
                                .<ModifierGroupModel>builder()
                                .success(true)
                                .message("Modifier Group Created Successfully")
                                .data(response)
                                .build()
                );
    }


    @PreAuthorize("hasAuthority('MODIFIER_GROUP_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ModifierGroupModel>>> search(
            @RequestBody ModifierGroupSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {


        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.valueOf(direction.toUpperCase()),
                                sortBy
                        )
                );


        return ResponseEntity.ok(
                ApiResponse
                        .<PageResponse<ModifierGroupModel>>builder()
                        .success(true)
                        .message("Modifier Groups fetched successfully")
                        .data(modifierGroupHandler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('MODIFIER_GROUP_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModifierGroupModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ModifierGroupModel model) {


        ModifierGroupModel response =
                modifierGroupHandler.update(id, model);


        sendEvent(
                "modifier-group-updated",
                response);


        return ResponseEntity.ok(
                ApiResponse
                        .<ModifierGroupModel>builder()
                        .success(true)
                        .message("Modifier Group Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('MODIFIER_GROUP_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {


        ModifierGroupModel deleted =
                modifierGroupHandler.delete(id);


        sendEvent(
                "modifier-group-deleted",
                deleted);


        return ResponseEntity.ok(
                ApiResponse
                        .<Void>builder()
                        .success(true)
                        .message("Modifier Group Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('MODIFIER_GROUP_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {


        ModifierGroupModel restored =
                modifierGroupHandler.restore(id);


        sendEvent(
                "modifier-group-restored",
                restored);


        return ResponseEntity.ok(
                ApiResponse
                        .<Void>builder()
                        .success(true)
                        .message("Modifier Group Restored Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('MODIFIER_GROUP_VIEW')")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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