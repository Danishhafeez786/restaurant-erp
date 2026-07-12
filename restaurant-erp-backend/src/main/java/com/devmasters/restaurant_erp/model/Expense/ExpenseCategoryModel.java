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
public class ExpenseCategoryModel {

    private UUID id;

    private String categoryName;

    private String categoryCode;

    private String description;

    private String color;

    private String icon;

    private Integer sortOrder;

    private Boolean systemDefined;

    private Boolean active;

    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
