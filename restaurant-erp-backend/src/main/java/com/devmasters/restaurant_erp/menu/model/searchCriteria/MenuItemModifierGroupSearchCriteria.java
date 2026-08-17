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
public class MenuItemModifierGroupSearchCriteria {

    private UUID menuItemId;

    private UUID modifierGroupId;

    private UUID organizationId;

    private UUID branchId;

    private Boolean required;

    private Boolean isActive;
}
