package com.devmasters.restaurant_erp.menu.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.AvailabilityStatus;
import com.devmasters.restaurant_erp.common.enums.MenuItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemSearchCriteria {

    private String name;

    private String code;

    private MenuItemType itemType;

    private UUID categoryId;

    private UUID organizationId;

    private UUID branchId;

    private Boolean featured;

    private Boolean popular;

    private AvailabilityStatus availabilityStatus;

    private Boolean isActive;
}
