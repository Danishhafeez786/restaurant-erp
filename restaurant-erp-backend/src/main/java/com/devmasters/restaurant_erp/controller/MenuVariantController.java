package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.MenuVariantHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.Menu.MenuVariantModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuVariantSearchCriteria;
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
@RequestMapping("/api/menu-variant")
@RequiredArgsConstructor
public class MenuVariantController {

    private final MenuVariantHandler menuVariantHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<MenuVariantModel>> create(@RequestBody MenuVariantModel model) {

        MenuVariantModel response = menuVariantHandler.create(model);
        sendEvent("menu-variant-created", response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<MenuVariantModel>builder()
                                .success(true)
                                .message("Menu Variant Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<MenuVariantModel>>> search(
            @RequestBody MenuVariantSearchCriteria criteria,
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
                ApiResponse.<PageResponse<MenuVariantModel>>builder()
                        .success(true)
                        .message("Menu Variants fetched successfully")
                        .data(menuVariantHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuVariantModel>> update(@PathVariable UUID id, @RequestBody MenuVariantModel model) {

        MenuVariantModel response = menuVariantHandler.update(id, model);
        sendEvent("menu-variant-updated", response);
        return ResponseEntity.ok(
                ApiResponse.<MenuVariantModel>builder()
                        .success(true)
                        .message("Menu Variant Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        MenuVariantModel deleted = menuVariantHandler.delete(id);
        sendEvent("menu-variant-deleted", deleted);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Menu Variant Deleted Successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {

        MenuVariantModel restored = menuVariantHandler.restore(id);
        sendEvent("menu-variant-restored", restored);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Menu Variant Restored Successfully")
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
                                .data(data));

            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);

            }
        });
    }
}