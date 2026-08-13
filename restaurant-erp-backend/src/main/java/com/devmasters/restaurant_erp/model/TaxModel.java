package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.TaxCalculationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxModel {

    private UUID id;
    private String taxCode;
    private String taxName;
    private TaxCalculationType calculationType;
    private BigDecimal rate;
    private String description;
    private Boolean defaultTax;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
}