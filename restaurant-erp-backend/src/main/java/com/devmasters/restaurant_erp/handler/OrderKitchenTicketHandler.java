package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.enums.KitchenTicketStatus;
import com.devmasters.restaurant_erp.model.order.OrderKitchenTicketModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderKitchenTicketSearchCriteria;
import com.devmasters.restaurant_erp.service.OrderKitchenTicketService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.OrderKitchenTicketTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderKitchenTicketHandler {

    private final OrderKitchenTicketService orderKitchenTicketService;
    private final OrderKitchenTicketTransformer orderKitchenTicketTransformer;
    private final CodeGeneratorService codeGeneratorService;


    public OrderKitchenTicketModel create(OrderKitchenTicketModel model) {

        if (model == null)
            throw new RuntimeException("Kitchen ticket data is required.");

        if (model.getOrderModel() == null ||
                model.getOrderModel().getId() == null) {

            throw new RuntimeException("Order is required.");
        }

        if (model.getOrganizationModel() == null ||
                model.getOrganizationModel().getId() == null) {

            throw new RuntimeException("Organization is required.");
        }

        if (model.getBranchModel() == null ||
                model.getBranchModel().getId() == null) {

            throw new RuntimeException("Branch is required.");
        }

        UUID orderId = model.getOrderModel().getId();

        /*
         * One kitchen ticket per order.
         */
        if (orderKitchenTicketService.existsByOrderId(orderId)) {
            throw new RuntimeException(
                    "Kitchen ticket already exists for this order."
            );
        }

        /*
         * Generate ticket number on server.
         *
         * Example:
         * KIT000001
         * KIT000002
         */
        String ticketNumber =
                codeGeneratorService.generateKitchenTicketCode(
                        model.getBranchModel().getId()
                );

        model.setTicketNumber(ticketNumber);

        /*
         * Default status.
         */
        if (model.getStatus() == null) {
            model.setStatus(KitchenTicketStatus.PENDING);
        }

        /*
         * Default priority.
         */
        if (model.getPriority() == null) {
            model.setPriority(0);
        }

        /*
         * Sent time.
         */
        if (model.getSentAt() == null) {
            model.setSentAt(LocalDateTime.now());
        }

        OrderKitchenTicket entity =
                orderKitchenTicketTransformer.toEntity(model);

        OrderKitchenTicket saved =
                orderKitchenTicketService.create(entity);

        return orderKitchenTicketTransformer.toModel(saved);
    }


    public PageResponse<OrderKitchenTicketModel> getAll(
            OrderKitchenTicketSearchCriteria criteria,
            Pageable pageable) {

        Page<OrderKitchenTicket> page =
                orderKitchenTicketService.search(
                        criteria,
                        pageable
                );

        return PageResponse.<OrderKitchenTicketModel>builder()
                .content(
                        orderKitchenTicketTransformer.toModels(
                                page.getContent()
                        )
                )
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }


    public OrderKitchenTicketModel getById(UUID id) {

        return orderKitchenTicketTransformer.toModel(
                orderKitchenTicketService.findById(id)
        );
    }


    public OrderKitchenTicketModel update(
            UUID id,
            OrderKitchenTicketModel model) {

        OrderKitchenTicket existing =
                orderKitchenTicketService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException(
                    "Inactive kitchen ticket cannot be updated."
            );
        }

        if (existing.getStatus() ==
                KitchenTicketStatus.COMPLETED) {

            throw new RuntimeException(
                    "Completed kitchen ticket cannot be updated."
            );
        }

        if (existing.getStatus() ==
                KitchenTicketStatus.CANCELLED) {

            throw new RuntimeException(
                    "Cancelled kitchen ticket cannot be updated."
            );
        }

        /*
         * Never allow ticket number to be changed.
         */
        model.setTicketNumber(existing.getTicketNumber());

        /*
         * Never allow order to be changed.
         */
        model.setOrderModel(
                new com.devmasters.restaurant_erp.model.order.OrderModel()
        );

        model.getOrderModel().setId(
                existing.getOrder().getId()
        );

        OrderKitchenTicket entity =
                orderKitchenTicketTransformer.toEntity(model);

        OrderKitchenTicket updated =
                orderKitchenTicketService.update(id, entity);

        return orderKitchenTicketTransformer.toModel(updated);
    }


    public OrderKitchenTicketModel delete(UUID id) {

        OrderKitchenTicket existing =
                orderKitchenTicketService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException(
                    "Kitchen ticket is already inactive."
            );
        }

        if (existing.getStatus() ==
                KitchenTicketStatus.COMPLETED) {

            throw new RuntimeException(
                    "Completed kitchen ticket cannot be deleted."
            );
        }

        return orderKitchenTicketTransformer.toModel(
                orderKitchenTicketService.delete(id)
        );
    }


    public OrderKitchenTicketModel restore(UUID id) {

        OrderKitchenTicket existing =
                orderKitchenTicketService.findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException(
                    "Kitchen ticket is already active."
            );
        }

        return orderKitchenTicketTransformer.toModel(
                orderKitchenTicketService.restore(id)
        );
    }
}