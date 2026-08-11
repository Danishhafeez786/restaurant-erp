package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_status_history")
public class OrderStatusHistory extends BaseEntity {

    private OrderStatus previousStatus;

    private OrderStatus newStatus;

    private String reason;

    private LocalDateTime changedAt;


    @DBRef
    private Order order;

    @DBRef
    private User changedBy;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}