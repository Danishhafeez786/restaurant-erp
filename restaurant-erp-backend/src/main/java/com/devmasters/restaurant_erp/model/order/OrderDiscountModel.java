package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.OrderDiscountType;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
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
public class OrderDiscountModel {

    private UUID id;
    private String discountNumber;
    private UUID orderId;
    private String discountName;
    private OrderDiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountAmount;
    private String reason;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private EmployeeModel appliedByModel;
}