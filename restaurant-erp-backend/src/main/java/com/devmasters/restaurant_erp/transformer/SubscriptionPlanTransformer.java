package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.model.SubscriptionModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class SubscriptionPlanTransformer extends Transformer<SubscriptionPlan, SubscriptionModel>{

    @Override
    public SubscriptionPlan toEntity(SubscriptionModel model) {
        if(model == null)
            return null;
        return SubscriptionPlan.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .name(model.getName())
                .branchesLimit(model.getBranchesLimit())
                .usersLimit(model.getUsersLimit())
                .menuItemsLimit(model.getMenuItemsLimit())
                .ordersPerMonth(model.getOrdersPerMonth())
                .monthlyPrice(model.getMonthlyPrice())
                .yearlyPrice(model.getYearlyPrice())
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public SubscriptionModel toModel(SubscriptionPlan entity) {
        if(entity == null)
            return null;
        return SubscriptionModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .branchesLimit(entity.getBranchesLimit())
                .usersLimit(entity.getUsersLimit())
                .menuItemsLimit(entity.getMenuItemsLimit())
                .ordersPerMonth(entity.getOrdersPerMonth())
                .monthlyPrice(entity.getMonthlyPrice())
                .yearlyPrice(entity.getYearlyPrice())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
