package com.devmasters.restaurant_erp.order.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.RefundReason;
import com.devmasters.restaurant_erp.common.enums.RefundStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundSearchCriteria {
    private String keyword;
    private UUID orderId;
    private UUID orderPaymentId;
    private RefundStatus status;
    private RefundReason reason;
    private BigDecimal minRefundAmount;
    private BigDecimal maxRefundAmount;
    private UUID organizationId;
    private UUID branchId;
    private UUID processedById;
    private Boolean isActive;
}