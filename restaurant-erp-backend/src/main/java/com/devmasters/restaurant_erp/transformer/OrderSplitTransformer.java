package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderSplit;
import com.devmasters.restaurant_erp.domain.order.OrderSplitItem;
import com.devmasters.restaurant_erp.model.order.OrderSplitItemModel;
import com.devmasters.restaurant_erp.model.order.OrderSplitModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderSplitTransformer {

    private final OrderTransformer orderTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    public OrderSplit toEntity(OrderSplitModel model) {
        if (model == null) return null;

        return OrderSplit.builder()
                .id(model.getId())
                .splitNumber(model.getSplitNumber())
                .splitSequence(model.getSplitSequence())
                .subtotal(model.getSubtotal())
                .discountAmount(model.getDiscountAmount())
                .taxAmount(model.getTaxAmount())
                .totalAmount(model.getTotalAmount())
                .paid(model.getPaid())
                .note(model.getNote())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .items(toItemsEntity(model.getItems()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public OrderSplitModel toModel(OrderSplit entity) {
        if (entity == null) return null;

        return OrderSplitModel.builder()
                .id(entity.getId())
                .splitNumber(entity.getSplitNumber())
                .splitSequence(entity.getSplitSequence())
                .subtotal(entity.getSubtotal())
                .discountAmount(entity.getDiscountAmount())
                .taxAmount(entity.getTaxAmount())
                .totalAmount(entity.getTotalAmount())
                .paid(entity.getPaid())
                .note(entity.getNote())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .organizationModel(
                        organizationTransformer.toModel(entity.getOrganization())
                )
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .items(toItemsModel(entity.getItems()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private List<OrderSplitItem> toItemsEntity(
            List<OrderSplitItemModel> models) {

        if (models == null) return null;

        return models.stream()
                .map(model -> OrderSplitItem.builder()
                        .orderItemId(model.getOrderItemId())
                        .quantity(model.getQuantity())
                        .unitPrice(model.getUnitPrice())
                        .totalAmount(model.getTotalAmount())
                        .build())
                .toList();
    }

    private List<OrderSplitItemModel> toItemsModel(
            List<OrderSplitItem> entities) {

        if (entities == null) return null;

        return entities.stream()
                .map(item -> OrderSplitItemModel.builder()
                        .orderItemId(item.getOrderItemId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalAmount(item.getTotalAmount())
                        .build())
                .toList();
    }

    public List<OrderSplitModel> toModels(
            List<OrderSplit> entities) {

        if (entities == null) return null;

        return entities.stream()
                .map(this::toModel)
                .toList();
    }
}