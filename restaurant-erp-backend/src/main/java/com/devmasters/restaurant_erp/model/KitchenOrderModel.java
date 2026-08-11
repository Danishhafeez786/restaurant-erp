package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.KitchenStatus;
import com.devmasters.restaurant_erp.model.order.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KitchenOrderModel {

    private UUID id;

    @Valid
    @NotNull(message = "Order is required")
    private OrderModel orderModel;

    @NotNull(message = "Kitchen status is required")
    private KitchenStatus kitchenStatus;

    @NotNull(message = "Estimated preparation time is required")
    @Min(value = 1, message = "Estimated preparation time must be at least 1 minute")
    private Integer estimatedMinutes;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
