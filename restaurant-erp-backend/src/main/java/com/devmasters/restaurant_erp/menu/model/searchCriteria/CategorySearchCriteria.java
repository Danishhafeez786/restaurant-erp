package com.devmasters.restaurant_erp.menu.model.searchCriteria;

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

    private String searchInput;

    private UUID organizationId;

    private UUID branchId;

    private Boolean isActive;
}
