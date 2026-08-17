package com.devmasters.restaurant_erp.order.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.order.domain.OrderDiscount;
import com.devmasters.restaurant_erp.employee.transformer.EmployeeTransformer;
import com.devmasters.restaurant_erp.order.model.OrderDiscountModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderDiscountTransformer extends Transformer<OrderDiscount, OrderDiscountModel> {

    private final OrderTransformer orderTransformer;
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
                .discountName(model.getDiscountName())
                .discountType(model.getDiscountType())
                .discountValue(model.getDiscountValue())
                .discountAmount(model.getDiscountAmount())
                .taxableAmount(model.getTaxableAmount())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .appliedBy(employeeTransformer.toEntity(model.getAppliedByModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public OrderDiscountModel toModel(OrderDiscount entity) {
        if (entity == null)
            return null;

        return OrderDiscountModel.builder()
                .id(entity.getId())
                .discountNumber(entity.getDiscountNumber())
                .discountName(entity.getDiscountName())
                .discountType(entity.getDiscountType())
                .discountValue(entity.getDiscountValue())
                .discountAmount(entity.getDiscountAmount())
                .taxableAmount(entity.getTaxableAmount())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .appliedByModel(employeeTransformer.toModel(entity.getAppliedBy()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}