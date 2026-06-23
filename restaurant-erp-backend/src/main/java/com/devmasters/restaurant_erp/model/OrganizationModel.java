package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.domain.SubscriptionPlan;
import com.devmasters.restaurant_erp.enums.BillingCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationModel {
    private UUID id;
    private String organizationName;
    private String logoUrl;
    private String ownerName;
    private String contactNumber;
    private String email;
    private String address;
    private String city;
    private String country;
    private SubscriptionModel subscriptionModel;
    private BillingCycle billingCycle;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
