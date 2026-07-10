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
public class CategorySearchCriteria {

    private String categoryCode;

    private String categoryName;

    private UUID organizationId;

    private UUID branchId;

    private Boolean available;

    private Boolean isActive;
}
