package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.TaxModel;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
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