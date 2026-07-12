package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.OrganizationModel;
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
public class ExpenseStatusModel {

    private UUID id;

    private String statusName;

    private String code;

    private String description;

    private String color;

    private Integer displayOrder;

    private Boolean defaultStatus;

    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}