package com.devmasters.restaurant_erp.order.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.order.domain.OrderDelivery;
import com.devmasters.restaurant_erp.employee.transformer.EmployeeTransformer;
import com.devmasters.restaurant_erp.order.model.OrderDeliveryModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderDeliveryTransformer extends Transformer<OrderDelivery, OrderDeliveryModel> {

    private final OrderTransformer orderTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final EmployeeTransformer employeeTransformer;

    @Override
    public OrderDelivery toEntity(OrderDeliveryModel model) {
        if (model == null)
            return null;

        return OrderDelivery.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .deliveryAddress(model.getDeliveryAddress())
                .deliveryInstructions(model.getDeliveryInstructions())
                .deliveryPartnerId(model.getDeliveryPartnerId())
                .status(model.getStatus())
                .assignedAt(model.getAssignedAt())
                .pickedUpAt(model.getPickedUpAt())
                .deliveredAt(model.getDeliveredAt())
                .cancelledAt(model.getCancelledAt())
                .cancellationReason(model.getCancellationReason())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .assignedBy(employeeTransformer.toEntity(model.getAssignedByModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public OrderDeliveryModel toModel(OrderDelivery entity) {
        if (entity == null)
            return null;

        return OrderDeliveryModel.builder()
                .id(entity.getId())
                .deliveryAddress(entity.getDeliveryAddress())
                .deliveryInstructions(entity.getDeliveryInstructions())
                .deliveryPartnerId(entity.getDeliveryPartnerId())
                .status(entity.getStatus())
                .assignedAt(entity.getAssignedAt())
                .pickedUpAt(entity.getPickedUpAt())
                .deliveredAt(entity.getDeliveredAt())
                .cancelledAt(entity.getCancelledAt())
                .cancellationReason(entity.getCancellationReason())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .assignedByModel(employeeTransformer.toModel(entity.getAssignedBy()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public List<OrderDeliveryModel> toModels(List<OrderDelivery> entities) {
        if (entities == null)
            return null;

        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<OrderDelivery> toEntities(List<OrderDeliveryModel> models) {
        if (models == null)
            return null;

        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}