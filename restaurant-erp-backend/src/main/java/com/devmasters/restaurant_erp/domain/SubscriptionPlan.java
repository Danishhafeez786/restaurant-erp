package com.devmasters.restaurant_erp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("subscription_plans")
public class SubscriptionPlan extends BaseEntity {
    private String name;
    private Integer branchesLimit;
    private Integer usersLimit;
    private Integer menuItemsLimit;
    private Integer ordersPerMonth;
    private Double monthlyPrice;
    private Double yearlyPrice;
}
