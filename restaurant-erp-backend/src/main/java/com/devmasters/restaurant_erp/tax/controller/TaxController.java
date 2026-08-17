package com.devmasters.restaurant_erp.tax.controller;

import com.devmasters.restaurant_erp.tax.handler.TaxHandler;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.tax.model.TaxModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.tax.model.searchCriteria.TaxSearchCriteria;
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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/tax")
@RequiredArgsConstructor
public class TaxController {

    private final TaxHandler taxHandler;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PreAuthorize("hasAuthority('TAX_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<TaxModel>> create(
            @Valid @RequestBody TaxModel model) {

        TaxModel response = taxHandler.create(model);
        sendEvent("tax-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<TaxModel>builder()
                        .success(true)
                        .message("Tax Created Successfully")
                        .data(response)
                        .build());
    }

    @PreAuthorize("hasAuthority('TAX_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<TaxModel>>> search(
            @RequestBody TaxSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.valueOf(direction.toUpperCase()),
                        sortBy
                )
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<TaxModel>>builder()
                        .success(true)
                        .message("Taxes Fetched Successfully")
                        .data(taxHandler.getAll(criteria, pageable))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TAX_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxModel>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<TaxModel>builder()
                        .success(true)
                        .message("Tax Fetched Successfully")
                        .data(taxHandler.getById(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TAX_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxModel>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaxModel model) {

        TaxModel response = taxHandler.update(id, model);
        sendEvent("tax-updated", response);

        return ResponseEntity.ok(
                ApiResponse.<TaxModel>builder()
                        .success(true)
                        .message("Tax Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TAX_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        TaxModel response = taxHandler.delete(id);
        sendEvent("tax-deleted", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Tax Deleted Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TAX_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        TaxModel response = taxHandler.restore(id);
        sendEvent("tax-restored", response);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Tax Restored Successfully")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('TAX_VIEW')")
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
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        });
    }
}