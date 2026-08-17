package com.devmasters.restaurant_erp.customer.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.customer.domain.Customer;
import com.devmasters.restaurant_erp.customer.model.CustomerModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CustomerTransformer extends Transformer<Customer, CustomerModel> {
    private final BranchTransformer branchTransformer;
    @Override
    public Customer toEntity(CustomerModel model) {
        if(model == null)
            return null;
        return Customer.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .customerCode(model.getCustomerCode())
                .fullName(model.getFullName())
                .phone(model.getPhone())
                .email(model.getEmail())
                .address(model.getAddress())
                .loyaltyPoints(model.getLoyaltyPoints())
                .creditBalance(model.getCreditBalance())
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .dateOfBirth(model.getDateOfBirth())
                .gender(model.getGender())
                .totalOrders(model.getTotalOrders())
                .totalSpent(model.getTotalSpent())
                .lastOrderDate(model.getLastOrderDate())
                .membershipLevel(model.getMembershipLevel())
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public CustomerModel toModel(Customer entity) {
        if(entity == null)
            return null;
        return CustomerModel.builder()
                .id(entity.getId())
                .customerCode(entity.getCustomerCode())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .loyaltyPoints(entity.getLoyaltyPoints())
                .creditBalance(entity.getCreditBalance())
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .dateOfBirth(entity.getDateOfBirth())
                .gender(entity.getGender())
                .totalOrders(entity.getTotalOrders())
                .totalSpent(entity.getTotalSpent())
                .lastOrderDate(entity.getLastOrderDate())
                .membershipLevel(entity.getMembershipLevel())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
