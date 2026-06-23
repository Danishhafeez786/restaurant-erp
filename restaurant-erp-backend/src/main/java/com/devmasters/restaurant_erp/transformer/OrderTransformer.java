package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Order;
import com.devmasters.restaurant_erp.model.OrderModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderTransformer extends Transformer<Order, OrderModel>{
    private OrganizationTransformer organizationTransformer;
    private BranchTransformer branchTransformer;
    private CustomerTransformer customerTransformer;
    private RestaurantTableTransformer restaurantTableTransformer;
    private UserTransformer userTransformer;

    @Override
    public Order toEntity(OrderModel model) {
        if(model == null)
            return null;
        return Order.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .orderNumber(model.getOrderNumber())
                .orderType(model.getOrderType())
                .paymentStatus(model.getPaymentStatus())
                .persons(model.getPersons())
                .grossAmount(model.getGrossAmount())
                .discountAmount(model.getDiscountAmount())
                .taxAmount(model.getTaxAmount())
                .netAmount(model.getNetAmount())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .customer(customerTransformer.toEntity(model.getCustomerModel()))
                .restaurantTable(restaurantTableTransformer.toEntity(model.getRestaurantTableModel()))
                .createdBy(userTransformer.toEntity(model.getCreatedBy()))
                .items(model.getItems())
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public OrderModel toModel(Order entity) {
        if(entity == null)
            return null;
        return OrderModel.builder()
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())
                .orderType(entity.getOrderType())
                .paymentStatus(entity.getPaymentStatus())
                .persons(entity.getPersons())
                .grossAmount(entity.getGrossAmount())
                .discountAmount(entity.getDiscountAmount())
                .taxAmount(entity.getTaxAmount())
                .netAmount(entity.getNetAmount())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .customerModel(customerTransformer.toModel(entity.getCustomer()))
                .restaurantTableModel(restaurantTableTransformer.toModel(entity.getRestaurantTable()))
                .createdBy(userTransformer.toModel(entity.getCreatedBy()))
                .items(entity.getItems())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
