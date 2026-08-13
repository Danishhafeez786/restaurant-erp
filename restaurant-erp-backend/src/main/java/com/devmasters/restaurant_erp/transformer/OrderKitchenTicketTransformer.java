package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.model.order.OrderKitchenTicketModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderKitchenTicketTransformer
        extends Transformer<OrderKitchenTicket, OrderKitchenTicketModel> {

    private final OrderTransformer orderTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final EmployeeTransformer employeeTransformer;

    @Override
    public OrderKitchenTicket toEntity(OrderKitchenTicketModel model) {

        if (model == null)
            return null;

        return OrderKitchenTicket.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .ticketNumber(model.getTicketNumber())
                .status(model.getStatus())
                .priority(model.getPriority())
                .kitchenNote(model.getKitchenNote())
                .sentAt(model.getSentAt())
                .acceptedAt(model.getAcceptedAt())
                .preparingAt(model.getPreparingAt())
                .readyAt(model.getReadyAt())
                .completedAt(model.getCompletedAt())
                .cancelledAt(model.getCancelledAt())
                .cancellationReason(model.getCancellationReason())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .assignedTo(employeeTransformer.toEntity(model.getAssignedToModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public OrderKitchenTicketModel toModel(OrderKitchenTicket entity) {

        if (entity == null)
            return null;

        return OrderKitchenTicketModel.builder()
                .id(entity.getId())
                .ticketNumber(entity.getTicketNumber())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .kitchenNote(entity.getKitchenNote())
                .sentAt(entity.getSentAt())
                .acceptedAt(entity.getAcceptedAt())
                .preparingAt(entity.getPreparingAt())
                .readyAt(entity.getReadyAt())
                .completedAt(entity.getCompletedAt())
                .cancelledAt(entity.getCancelledAt())
                .cancellationReason(entity.getCancellationReason())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .assignedToModel(employeeTransformer.toModel(entity.getAssignedTo()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public List<OrderKitchenTicketModel> toModels(
            List<OrderKitchenTicket> entities) {

        if (entities == null)
            return null;

        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<OrderKitchenTicket> toEntities(
            List<OrderKitchenTicketModel> models) {

        if (models == null)
            return null;

        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}