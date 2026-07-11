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
public class ModifierSearchCriteria {

    private String name;

    private String code;

    private String sku;

    private UUID modifierGroupId;

    private UUID organizationId;

    private UUID branchId;

    private Boolean inventoryTracked;

    private Boolean available;

    private Boolean isActive;
}
