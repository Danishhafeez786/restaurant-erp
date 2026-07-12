package com.devmasters.restaurant_erp.model.searchcriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseStatusSearchCriteria {

    private String statusName;

    private String code;

    private String color;

    private UUID organizationId;

    private Boolean defaultStatus;

    private Boolean isActive;
}