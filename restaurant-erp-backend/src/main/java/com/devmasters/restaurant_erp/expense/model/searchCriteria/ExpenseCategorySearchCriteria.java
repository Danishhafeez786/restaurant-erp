package com.devmasters.restaurant_erp.expense.model.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategorySearchCriteria {

    private String categoryName;

    private String categoryCode;

    private String description;

    private UUID organizationId;

    private Boolean systemDefined;

    private Boolean isActive;
}
