package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.TaxCalculationType;
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