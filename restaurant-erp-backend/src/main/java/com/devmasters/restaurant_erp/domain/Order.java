package com.devmasters.restaurant_erp.domain;

import com.devmasters.restaurant_erp.domain.subclass.OrderItem;
import com.devmasters.restaurant_erp.enums.OrderType;
import com.devmasters.restaurant_erp.enums.PaymentStatus;
import com.devmasters.restaurant_erp.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("orders")
public class Order extends BaseEntity {
    private String orderNumber;
    private OrderType orderType;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Integer persons;
    private Double grossAmount;
    private Double discountAmount;
    private Double taxAmount;
    private Double netAmount;
    @DBRef
    private Organization organization;
    @DBRef
    private Branch branch;
    @DBRef
    private Customer customer;
    @DBRef
    private RestaurantTable restaurantTable;
    @DBRef
    private User createdBy;
    private List<OrderItem> items;
}
