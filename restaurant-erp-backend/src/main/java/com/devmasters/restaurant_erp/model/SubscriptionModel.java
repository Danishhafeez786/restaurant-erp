package com.devmasters.restaurant_erp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionModel {
    private UUID id;
    private String name;
    private Integer branchesLimit;
    private Integer usersLimit;
    private Integer menuItemsLimit;
    private Integer ordersPerMonth;
    private Double monthlyPrice;
    private Double yearlyPrice;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
