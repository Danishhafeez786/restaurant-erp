package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.OrderKitchenTicketHandler;
import com.devmasters.restaurant_erp.model.ApiResponse;
import com.devmasters.restaurant_erp.model.order.OrderKitchenTicketModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderKitchenTicketSearchCriteria;
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
@RequestMapping("/api/order-kitchen-ticket")
@RequiredArgsConstructor
public class OrderKitchenTicketController {

    private final OrderKitchenTicketHandler orderKitchenTicketHandler;

    private final List<SseEmitter> emitters =
            new CopyOnWriteArrayList<>();


    // =========================================================
    // CREATE
    // =========================================================

    @PreAuthorize("hasAuthority('ORDER_KITCHEN_TICKET_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderKitchenTicketModel>> create(
            @RequestBody OrderKitchenTicketModel model) {

        OrderKitchenTicketModel response =
                orderKitchenTicketHandler.create(model);

        sendEvent(
                "order-kitchen-ticket-created",
                response
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<OrderKitchenTicketModel>builder()
                                .success(true)
                                .message(
                                        "Order Kitchen Ticket Created Successfully"
                                )
                                .data(response)
                                .build()
                );
    }


    // =========================================================
    // SEARCH
    // =========================================================

    @PreAuthorize("hasAuthority('ORDER_KITCHEN_TICKET_VIEW')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<OrderKitchenTicketModel>>> search(
            @RequestBody OrderKitchenTicketSearchCriteria criteria,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @RequestParam(defaultValue = "DESC")
            String direction) {

        Sort.Direction sortDirection =
                Sort.Direction.fromString(
                        direction
                );

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sortBy)
                );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<OrderKitchenTicketModel>>builder()
                        .success(true)
                        .message(
                                "Kitchen Tickets Fetched Successfully"
                        )
                        .data(
                                orderKitchenTicketHandler.getAll(
                                        criteria,
                                        pageable
                                )
                        )
                        .build()
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @PreAuthorize("hasAuthority('ORDER_KITCHEN_TICKET_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderKitchenTicketModel>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                ApiResponse.<OrderKitchenTicketModel>builder()
                        .success(true)
                        .message(
                                "Kitchen Ticket Fetched Successfully"
                        )
                        .data(
                                orderKitchenTicketHandler.getById(id)
                        )
                        .build()
        );
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PreAuthorize("hasAuthority('ORDER_KITCHEN_TICKET_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderKitchenTicketModel>> update(
            @PathVariable UUID id,
            @RequestBody OrderKitchenTicketModel model) {

        OrderKitchenTicketModel response =
                orderKitchenTicketHandler.update(
                        id,
                        model
                );

        sendEvent(
                "order-kitchen-ticket-updated",
                response
        );

        return ResponseEntity.ok(
                ApiResponse.<OrderKitchenTicketModel>builder()
                        .success(true)
                        .message(
                                "Kitchen Ticket Updated Successfully"
                        )
                        .data(response)
                        .build()
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @PreAuthorize("hasAuthority('ORDER_KITCHEN_TICKET_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id) {

        OrderKitchenTicketModel response =
                orderKitchenTicketHandler.delete(id);

        sendEvent(
                "order-kitchen-ticket-deleted",
                response
        );

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Kitchen Ticket Deleted Successfully"
                        )
                        .build()
        );
    }


    // =========================================================
    // RESTORE
    // =========================================================

    @PreAuthorize("hasAuthority('ORDER_KITCHEN_TICKET_RESTORE')")
    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<Void>> restore(
            @PathVariable UUID id) {

        OrderKitchenTicketModel response =
                orderKitchenTicketHandler.restore(id);

        sendEvent(
                "order-kitchen-ticket-restored",
                response
        );

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message(
                                "Kitchen Ticket Restored Successfully"
                        )
                        .build()
        );
    }


    // =========================================================
    // SSE STREAM
    // =========================================================

    @GetMapping(
            value = "/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter stream() {

        SseEmitter emitter =
                new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(
                () -> emitters.remove(emitter)
        );

        emitter.onTimeout(
                () -> emitters.remove(emitter)
        );

        emitter.onError(
                e -> emitters.remove(emitter)
        );

        return emitter;
    }


    // =========================================================
    // SEND SSE EVENT
    // =========================================================

    private void sendEvent(
            String eventName,
            Object data) {

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