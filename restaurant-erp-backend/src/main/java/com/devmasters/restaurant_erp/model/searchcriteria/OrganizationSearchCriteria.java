package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.BillingCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSearchCriteria {

    private String organizationName;

    private String ownerName;

    private String city;

    private String country;

    private Boolean isActive;

    private BillingCycle billingCycle;

    private UUID subscriptionPlanId;
}
