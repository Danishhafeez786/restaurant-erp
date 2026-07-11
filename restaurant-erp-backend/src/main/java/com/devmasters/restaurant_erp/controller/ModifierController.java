package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.ModifierHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.Menu.ModifierModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierSearchCriteria;
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
@RequestMapping("/api/modifier")
@RequiredArgsConstructor
public class ModifierController {

    private final ModifierHandler modifierHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<ModifierModel>> create(@RequestBody ModifierModel model) {

        ModifierModel response = modifierHandler.create(model);
        sendEvent("modifier-created", response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ModifierModel>builder()
                                .success(true)
                                .message(
                                        "Modifier Created Successfully")
                                .data(response)
                                .build()
                );
    }


    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ModifierModel>>> search(
            @RequestBody ModifierSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {
        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.valueOf(
                                        direction.toUpperCase()),
                                sortBy)
                );


        return ResponseEntity.ok(
                ApiResponse.<PageResponse<ModifierModel>>builder()
                        .success(true)
                        .message(
                                "Modifiers fetched successfully")
                        .data(
                                modifierHandler.getAll(
                                        criteria,
                                        pageable))
                        .build()
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ModifierModel>> update(@PathVariable UUID id,
            @RequestBody ModifierModel model) {
        ModifierModel response = modifierHandler.update(id, model);
        sendEvent(
                "modifier-updated",
                response);
        return ResponseEntity.ok(
                ApiResponse.<ModifierModel>builder()
                        .success(true)
                        .message(
                                "Modifier Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        ModifierModel deleted = modifierHandler.delete(id);
        sendEvent(
                "modifier-deleted",
                deleted);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Modifier Deleted Successfully")
                        .build()
        );
    }


    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {

        ModifierModel restored = modifierHandler.restore(id);
        sendEvent(
                "modifier-restored",
                restored);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Modifier Restored Successfully")
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
