package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrganizationTransformer extends Transformer<Organization, OrganizationModel> {
    private final SubscriptionPlanTransformer subscriptionPlanTransformer;

    @Override
    public Organization toEntity(OrganizationModel model) {
        if(model == null)
            return null;
        return Organization.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .organizationName(model.getOrganizationName())
                .logoUrl(model.getLogoUrl())
                .ownerName(model.getOwnerName())
                .contactNumber(model.getContactNumber())
                .email(model.getEmail())
                .address(model.getAddress())
                .city(model.getCity())
                .country(model.getCountry())
                .subscriptionPlan(subscriptionPlanTransformer.toEntity(model.getSubscriptionModel()))
                .billingCycle(model.getBillingCycle())
                .subscriptionStartDate(model.getSubscriptionStartDate())
                .subscriptionEndDate(model.getSubscriptionEndDate())
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public OrganizationModel toModel(Organization entity) {
        if(entity == null)
            return null;
        return OrganizationModel.builder()
                .id(entity.getId())
                .organizationName(entity.getOrganizationName())
                .logoUrl(entity.getLogoUrl())
                .ownerName(entity.getOwnerName())
                .contactNumber(entity.getContactNumber())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .city(entity.getCity())
                .country(entity.getCountry())
                .subscriptionModel(subscriptionPlanTransformer.toModel(entity.getSubscriptionPlan()))
                .billingCycle(entity.getBillingCycle())
                .subscriptionStartDate(entity.getSubscriptionStartDate())
                .subscriptionEndDate(entity.getSubscriptionEndDate())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
