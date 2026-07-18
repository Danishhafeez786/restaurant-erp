package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.EmployeeHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
import com.devmasters.restaurant_erp.model.employee.EmployeeRequestModel;
import com.devmasters.restaurant_erp.model.searchcriteria.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
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
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeHandler employeeHandler;

    private final List<SseEmitter> emitters =
            new CopyOnWriteArrayList<>();

    /**
     * Create Employee + User
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeModel>> create(
            @Valid @RequestBody EmployeeRequestModel model) {

        EmployeeModel response = employeeHandler.create(model);

        sendEvent("employee-created", response);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<EmployeeModel>builder()
                                .success(true)
                                .message("Employee Created Successfully")
                                .data(response)
                                .build()
                );
    }

    /**
     * Search Employees
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<EmployeeModel>>> search(
            @RequestBody EmployeeSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.valueOf(direction.toUpperCase()), sortBy));

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<EmployeeModel>>builder()
                        .success(true)
                        .message("Employees fetched successfully")
                        .data(employeeHandler.search(criteria, pageable))
                        .build()
        );
    }

    /**
     * Get Employee By Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeModel>> getById(@PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<EmployeeModel>builder()
                        .success(true)
                        .message("Employee fetched successfully")
                        .data(employeeHandler.findById(id))
                        .build()

        );
    }

    /**
     * Update Employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeModel>> update(
            @Valid @PathVariable UUID id, @RequestBody EmployeeModel model) {

        EmployeeModel response = employeeHandler.update(id, model);
        sendEvent("employee-updated", response);
        return ResponseEntity.ok(
                ApiResponse.<EmployeeModel>builder()
                        .success(true)
                        .message("Employee Updated Successfully")
                        .data(response)
                        .build()
        );
    }

    /**
     * Soft Delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        EmployeeModel deleted = employeeHandler.delete(id);
        sendEvent("employee-deleted", deleted);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Employee Deleted Successfully")
                        .build()
        );
    }

    /**
     * Restore Employee
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable UUID id) {
        EmployeeModel restored = employeeHandler.restore(id);
        sendEvent("employee-restored", restored);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Employee Restored Successfully")
                        .build()
        );
    }

    /**
     * SSE Stream
     */
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