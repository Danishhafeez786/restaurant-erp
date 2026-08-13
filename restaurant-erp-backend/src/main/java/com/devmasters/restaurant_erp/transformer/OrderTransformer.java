package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.Order;
import com.devmasters.restaurant_erp.model.order.OrderModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderTransformer extends Transformer<Order, OrderModel> {

    private final OrderItemTransformer orderItemTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final CustomerTransformer customerTransformer;
    private final RestaurantTableTransformer restaurantTableTransformer;
    private final UserTransformer userTransformer;

    @Override
    public OrderModel toModel(Order order) {
        if (order == null)
            return null;

        return OrderModel.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderType(order.getOrderType())
                .orderSource(order.getOrderSource())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .persons(order.getPersons())
                .subtotalAmount(order.getSubtotalAmount())
                .discountAmount(order.getDiscountAmount())
                .taxAmount(order.getTaxAmount())
                .serviceChargeAmount(order.getServiceChargeAmount())
                .deliveryChargeAmount(order.getDeliveryChargeAmount())
                .roundingAmount(order.getRoundingAmount())
                .totalAmount(order.getTotalAmount())
                .orderedAt(order.getOrderedAt())
                .confirmedAt(order.getConfirmedAt())
                .preparingAt(order.getPreparingAt())
                .readyAt(order.getReadyAt())
                .completedAt(order.getCompletedAt())
                .cancelledAt(order.getCancelledAt())
                .customerNote(order.getCustomerNote())
                .internalNote(order.getInternalNote())
                .cancellationReason(order.getCancellationReason())
                .tableSessionNumber(order.getTableSessionNumber())
                .organizationModel(organizationTransformer.toModel(order.getOrganization()))
                .branchModel(branchTransformer.toModel(order.getBranch()))
                .customerModel(customerTransformer.toModel(order.getCustomer()))
                .restaurantTableModel(restaurantTableTransformer.toModel(order.getRestaurantTable()))
                .createdByModel(userTransformer.toModel(order.getCreatedBy()))
                .updatedByModel(userTransformer.toModel(order.getUpdatedBy()))
                .cancelledByModel(userTransformer.toModel(order.getCancelledBy()))
                .items(orderItemTransformer.toModels(order.getItems()))
                .build();
    }

    @Override
    public Order toEntity(OrderModel model) {
        if (model == null)
            return null;

        return Order.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .orderNumber(model.getOrderNumber())
                .orderType(model.getOrderType())
                .orderSource(model.getOrderSource())
                .status(model.getStatus())
                .paymentStatus(model.getPaymentStatus())
                .persons(model.getPersons())
                .subtotalAmount(model.getSubtotalAmount())
                .discountAmount(model.getDiscountAmount())
                .taxAmount(model.getTaxAmount())
                .serviceChargeAmount(model.getServiceChargeAmount())
                .deliveryChargeAmount(model.getDeliveryChargeAmount())
                .roundingAmount(model.getRoundingAmount())
                .totalAmount(model.getTotalAmount())
                .orderedAt(model.getOrderedAt())
                .confirmedAt(model.getConfirmedAt())
                .preparingAt(model.getPreparingAt())
                .readyAt(model.getReadyAt())
                .completedAt(model.getCompletedAt())
                .cancelledAt(model.getCancelledAt())
                .customerNote(model.getCustomerNote())
                .internalNote(model.getInternalNote())
                .cancellationReason(model.getCancellationReason())
                .tableSessionNumber(model.getTableSessionNumber())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .customer(customerTransformer.toEntity(model.getCustomerModel()))
                .restaurantTable(restaurantTableTransformer.toEntity(model.getRestaurantTableModel()))
                .createdBy(userTransformer.toEntity(model.getCreatedByModel()))
                .updatedBy(userTransformer.toEntity(model.getUpdatedByModel()))
                .cancelledBy(userTransformer.toEntity(model.getCancelledByModel()))
                .items(orderItemTransformer.toEntities(model.getItems()))
                .build();
    }

    @Override
    public List<OrderModel> toModels(List<Order> entities) {
        if (entities == null)
            return List.of();

        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}