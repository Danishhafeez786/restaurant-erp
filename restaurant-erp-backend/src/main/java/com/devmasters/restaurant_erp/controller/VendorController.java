package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.VendorHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.VendorModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.VendorSearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorHandler handler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    @PreAuthorize("hasAuthority('VENDOR_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<VendorModel>> create(
            @Valid @RequestBody VendorModel model) {

        VendorModel response = handler.create(model);
        sendEvent("expense-vendor-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<VendorModel>builder()
                                .success(true)
                                .message("Expense Vendor Created Successfully")
                                .data(response)
                                .build()
                );
    }


    @PreAuthorize("hasAuthority('VENDOR_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<VendorModel>>> search(
            @RequestBody VendorSearchCriteria criteria,
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
                ApiResponse.<PageResponse<VendorModel>>builder()
                        .success(true)
                        .message("Expense Vendors fetched successfully")
                        .data(handler.getAll(criteria, pageable))
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('VENDOR_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody VendorModel model) {

        VendorModel response = handler.update(id, model);
        sendEvent("expense-vendor-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<VendorModel>builder()
                        .success(true)
                        .message("Expense Vendor Updated Successfully")
                        .data(response)
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('VENDOR_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        VendorModel response = handler.delete(id);
        sendEvent("expense-vendor-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Vendor Deleted Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('VENDOR_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        VendorModel response = handler.restore(id);
        sendEvent("expense-vendor-restored", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Expense Vendor Restored Successfully")
                        .build()
        );
    }


    @PreAuthorize("hasAuthority('VENDOR_VIEW')")
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