package com.devmasters.restaurant_erp.order.model;

import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.tax.model.TaxModel;
import com.devmasters.restaurant_erp.employee.model.EmployeeModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTaxModel {

    private UUID id;
    private String taxNumber;
    private OrderModel orderModel;
    private TaxModel taxModel;
    private String taxName;
    private BigDecimal taxRate;
    private BigDecimal taxableAmount;
    private BigDecimal taxAmount;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private EmployeeModel appliedByModel;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}