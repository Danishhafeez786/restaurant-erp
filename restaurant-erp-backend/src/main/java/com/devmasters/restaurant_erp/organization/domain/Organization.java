package com.devmasters.restaurant_erp.organization.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.common.enums.BillingCycle;
import com.devmasters.restaurant_erp.subscriptionplan.domain.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("organizations")
public class Organization extends BaseEntity {
    private String organizationName;
    private String logoUrl;
    private String ownerName;
    private String contactNumber;
    private String email;
    private String address;
    private String city;
    private String country;
    @DBRef
    private SubscriptionPlan subscriptionPlan;
    private BillingCycle billingCycle;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
}
