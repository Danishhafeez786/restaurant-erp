package com.devmasters.restaurant_erp.order.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.order.domain.OrderStatusHistory;
import com.devmasters.restaurant_erp.employee.transformer.EmployeeTransformer;
import com.devmasters.restaurant_erp.order.model.OrderStatusHistoryModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderStatusHistoryTransformer extends Transformer<OrderStatusHistory, OrderStatusHistoryModel> {

    private final OrderTransformer orderTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final EmployeeTransformer employeeTransformer;

    @Override
    public OrderStatusHistory toEntity(OrderStatusHistoryModel model) {
        if (model == null)
            return null;

        return OrderStatusHistory.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .previousStatus(model.getPreviousStatus())
                .newStatus(model.getNewStatus())
                .reason(model.getReason())
                .changedAt(model.getChangedAt())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .changedBy(employeeTransformer.toEntity(model.getChangedByModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public OrderStatusHistoryModel toModel(OrderStatusHistory entity) {
        if (entity == null)
            return null;

        return OrderStatusHistoryModel.builder()
                .id(entity.getId())
                .previousStatus(entity.getPreviousStatus())
                .newStatus(entity.getNewStatus())
                .reason(entity.getReason())
                .changedAt(entity.getChangedAt())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public List<OrderStatusHistoryModel> toModels(List<OrderStatusHistory> entities) {
        if (entities == null)
            return null;

        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<OrderStatusHistory> toEntities(List<OrderStatusHistoryModel> models) {
        if (models == null)
            return null;

        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}