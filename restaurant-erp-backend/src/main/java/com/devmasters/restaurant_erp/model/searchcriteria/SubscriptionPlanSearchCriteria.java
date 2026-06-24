package com.devmasters.restaurant_erp.model.searchcriteria;

import lombok.Data;

@Data
public class SubscriptionPlanSearchCriteria {

    private String name;

    private Boolean isActive;

    private Double minMonthlyPrice;

    private Double maxMonthlyPrice;

    private Integer minUsersLimit;

    private Integer maxUsersLimit;
}
