package com.devmasters.restaurant_erp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionModel {

        private UUID id;

        @NotBlank(message = "Subscription plan name is required")
        @Size(min = 2, max = 100, message = "Subscription plan name must be between 2 and 100 characters")
        private String name;

        @NotNull(message = "Branches limit is required")
        @Min(value = 1, message = "Branches limit must be at least 1")
        private Integer branchesLimit;

        @NotNull(message = "Users limit is required")
        @Min(value = 1, message = "Users limit must be at least 1")
        private Integer usersLimit;

        @NotNull(message = "Menu items limit is required")
        @Min(value = 1, message = "Menu items limit must be at least 1")
        private Integer menuItemsLimit;

        @NotNull(message = "Orders per month is required")
        @Min(value = 1, message = "Orders per month must be at least 1")
        private Integer ordersPerMonth;

        @NotNull(message = "Monthly price is required")
        @PositiveOrZero(message = "Monthly price cannot be negative")
        private Double monthlyPrice;

        @NotNull(message = "Yearly price is required")
        @PositiveOrZero(message = "Yearly price cannot be negative")
        private Double yearlyPrice;

        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
