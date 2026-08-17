package com.devmasters.restaurant_erp.menu.controller;

import com.devmasters.restaurant_erp.menu.handler.MenuItemModifierGroupHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.menu.model.MenuItemModifierGroupModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemModifierGroupSearchCriteria;
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
@RequestMapping("/api/menu-item-modifier-group")
@RequiredArgsConstructor
public class MenuItemModifierGroupController {

    private final MenuItemModifierGroupHandler handler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('MENU_ITEM_MODIFIER_GROUP_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<MenuItemModifierGroupModel>> create(
            @Valid @RequestBody MenuItemModifierGroupModel model) {

        MenuItemModifierGroupModel response = handler.create(model);
        sendEvent("menu-item-modifier-group-created", response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse
                                .<MenuItemModifierGroupModel>builder()
                                .success(true)
                                .message(
                                        "Menu Item Modifier Group Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PreAuthorize("hasAuthority('MENU_ITEM_MODIFIER_GROUP_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<MenuItemModifierGroupModel>>> search(
            @RequestBody MenuItemModifierGroupSearchCriteria criteria,
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
                ApiResponse
                        .<PageResponse<MenuItemModifierGroupModel>>builder()
                        .success(true)
                        .message(
                                "Menu Item Modifier Groups fetched successfully")
                        .data(
                                handler.getAll(
                                        criteria,
                                        pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('MENU_ITEM_MODIFIER_GROUP_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuItemModifierGroupModel>> update(@PathVariable UUID id,@Valid @RequestBody MenuItemModifierGroupModel model) {

        MenuItemModifierGroupModel response = handler.update(id, model);
        sendEvent("menu-item-modifier-group-updated", response);
        return ResponseEntity.ok(
                ApiResponse
                        .<MenuItemModifierGroupModel>builder()
                        .success(true)
                        .message(
                                "Menu Item Modifier Group Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('MENU_ITEM_MODIFIER_GROUP_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {

        MenuItemModifierGroupModel response = handler.delete(id);
        sendEvent("menu-item-modifier-group-deleted", response);
        return ResponseEntity.ok(
                ApiResponse
                        .<Void>builder()
                        .success(true)
                        .message(
                                "Menu Item Modifier Group Deleted Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('MENU_ITEM_MODIFIER_GROUP_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {

        MenuItemModifierGroupModel response = handler.restore(id);

        sendEvent("menu-item-modifier-group-restored", response);
        return ResponseEntity.ok(
                ApiResponse
                        .<Void>builder()
                        .success(true)
                        .message(
                                "Menu Item Modifier Group Restored Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('MENU_ITEM_MODIFIER_GROUP_VIEW')")
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
