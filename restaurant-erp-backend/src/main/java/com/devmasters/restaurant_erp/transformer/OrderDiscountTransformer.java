package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.model.order.OrderDiscountModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderDiscountTransformer extends Transformer<OrderDiscount, OrderDiscountModel> {

    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final EmployeeTransformer employeeTransformer;

    @Override
    public OrderDiscount toEntity(OrderDiscountModel model) {
        if (model == null)
            return null;

        return OrderDiscount.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .discountNumber(model.getDiscountNumber())
                .orderId(model.getOrderId())
                .discountName(model.getDiscountName())
                .discountType(model.getDiscountType())
                .discountValue(model.getDiscountValue())
                .discountAmount(model.getDiscountAmount())
                .reason(model.getReason())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .appliedBy(employeeTransformer.toEntity(model.getAppliedByModel()))
                .build();
    }

    @Override
    public OrderDiscountModel toModel(OrderDiscount entity) {
        if (entity == null)
            return null;

        return OrderDiscountModel.builder()
                .id(entity.getId())
                .discountNumber(entity.getDiscountNumber())
                .orderId(entity.getOrderId())
                .discountName(entity.getDiscountName())
                .discountType(entity.getDiscountType())
                .discountValue(entity.getDiscountValue())
                .discountAmount(entity.getDiscountAmount())
                .reason(entity.getReason())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .appliedByModel(employeeTransformer.toModel(entity.getAppliedBy()))
                .build();
    }
}