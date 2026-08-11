package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_deliveries")
public class OrderDelivery extends BaseEntity {

    private DeliveryStatus status;

    private String deliveryAddress;

    private String deliveryInstructions;

    private LocalDateTime assignedAt;

    private LocalDateTime pickedUpAt;

    private LocalDateTime deliveredAt;

    private String cancellationReason;


    @DBRef
    private Order order;

    @DBRef
    private UUID deliveryPartnerId;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}