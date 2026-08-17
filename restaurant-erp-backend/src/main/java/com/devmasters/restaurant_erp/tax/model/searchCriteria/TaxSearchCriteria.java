package com.devmasters.restaurant_erp.tax.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.TaxCalculationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxSearchCriteria {

    private String searchInput;
    private TaxCalculationType calculationType;
    private UUID organizationId;
    private UUID branchId;
    private Boolean defaultTax;
    private Boolean isActive;
}