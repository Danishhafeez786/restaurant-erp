package com.devmasters.restaurant_erp.order.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.order.domain.OrderTax;
import com.devmasters.restaurant_erp.employee.transformer.EmployeeTransformer;
import com.devmasters.restaurant_erp.order.model.OrderTaxModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import com.devmasters.restaurant_erp.tax.transformer.TaxTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderTaxTransformer extends Transformer<OrderTax, OrderTaxModel> {

    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final EmployeeTransformer employeeTransformer;
    private final OrderTransformer orderTransformer;
    private final TaxTransformer taxTransformer;

    @Override
    public OrderTax toEntity(OrderTaxModel model) {
        if (model == null)
            return null;

        return OrderTax.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .taxNumber(model.getTaxNumber())
                .taxName(model.getTaxName())
                .taxRate(model.getTaxRate())
                .taxableAmount(model.getTaxableAmount())
                .taxAmount(model.getTaxAmount())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .tax(taxTransformer.toEntity(model.getTaxModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .appliedBy(employeeTransformer.toEntity(model.getAppliedByModel()))
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public OrderTaxModel toModel(OrderTax entity) {
        if (entity == null)
            return null;

        return OrderTaxModel.builder()
                .id(entity.getId())
                .taxNumber(entity.getTaxNumber())
                .taxName(entity.getTaxName())
                .taxRate(entity.getTaxRate())
                .taxableAmount(entity.getTaxableAmount())
                .taxAmount(entity.getTaxAmount())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .taxModel(taxTransformer.toModel(entity.getTax()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .appliedByModel(employeeTransformer.toModel(entity.getAppliedBy()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}