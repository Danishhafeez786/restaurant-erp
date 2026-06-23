package com.devmasters.restaurant_erp.model;


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
public class RestaurantTableModel {
    private UUID id;
    private String tableNumber;
    private Integer capacity;
    private String qrToken;
    private BranchModel branchModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
