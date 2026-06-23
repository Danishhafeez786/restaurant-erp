package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.KitchenStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KitchenOrderModel {
    private UUID id;
    private OrderModel orderModel;
    private KitchenStatus kitchenStatus;
    private Integer estimatedMinutes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
