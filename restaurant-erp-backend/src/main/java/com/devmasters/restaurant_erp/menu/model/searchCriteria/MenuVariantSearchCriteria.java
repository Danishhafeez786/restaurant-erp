package com.devmasters.restaurant_erp.menu.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuVariantSearchCriteria {

    private String name;

    private String code;

    private String sku;

    private String barcode;

    private UUID menuItemId;

    private UUID categoryId;

    private UUID organizationId;

    private UUID branchId;

    private Boolean defaultVariant;

    private Boolean inventoryTracked;

    private AvailabilityStatus availabilityStatus;

    private Boolean isActive;
}
