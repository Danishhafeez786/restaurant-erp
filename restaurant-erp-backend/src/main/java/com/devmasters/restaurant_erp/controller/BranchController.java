package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.BranchHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.BranchSearchCriteria;
import jakarta.validation.Valid;
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
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchController {

    private final BranchHandler branchHandler;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<ApiResponse<BranchModel>> create(@Valid @RequestBody BranchModel model) {
        BranchModel response = branchHandler.create(model);
        sendEvent("branch-created", response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<BranchModel>builder()
                                .success(true)
                                .message("Branch Created Successfully")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<BranchModel>>> search(
            @RequestBody BranchSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()),sortBy));
        return ResponseEntity.ok(ApiResponse.<PageResponse<BranchModel>>builder()
                    .success(true)
                    .message("Branches fetched successfully")
                    .data(branchHandler.getAll(criteria, pageable))
                    .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchModel>> update(@Valid @PathVariable UUID id,
            @RequestBody BranchModel model) {
        BranchModel response = branchHandler.update(id, model);
        sendEvent("branch-updated", response);
        return ResponseEntity.ok(ApiResponse.<BranchModel>builder()
                        .success(true)
                        .message("Branch Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        BranchModel deleted = branchHandler.delete(id);
        sendEvent("branch-deleted", deleted);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Branch Deleted Successfully")
                        .build()
        );
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {
        BranchModel restored = branchHandler.restore(id);
        sendEvent("branch-restored", restored);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .success(true)
                        .message("Branch Restored Successfully")
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