package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.DiscountType;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
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
public class OrderDiscountModel {

    private UUID id;
    private String discountNumber;
    private String discountName;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountAmount;
    private BigDecimal taxableAmount;
    private OrderModel orderModel;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private EmployeeModel appliedByModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}