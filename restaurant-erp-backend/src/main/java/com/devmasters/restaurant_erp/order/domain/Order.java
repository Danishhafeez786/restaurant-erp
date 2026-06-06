package com.devmasters.restaurant_erp.order.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import com.devmasters.restaurant_erp.common.enums.OrderStatus;
import com.devmasters.restaurant_erp.common.enums.OrderType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order extends BaseDomain {

    private String orderNo;

    private String branchId;

    private String customerId;

    private String tableId;

    private String waiterId;

    private OrderType orderType;

    private OrderStatus orderStatus;

    private BigDecimal subtotal;

    private BigDecimal tax;

    private BigDecimal discount;

    private BigDecimal grandTotal;

    private List<OrderItem> items;
}