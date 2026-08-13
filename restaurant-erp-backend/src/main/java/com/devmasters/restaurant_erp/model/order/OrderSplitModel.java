package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSplitModel {
    private UUID id;
    private String splitNumber;
    private Integer splitSequence;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private Boolean paid;
    private String note;
    private OrderModel orderModel;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private List<OrderSplitItemModel> items;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}