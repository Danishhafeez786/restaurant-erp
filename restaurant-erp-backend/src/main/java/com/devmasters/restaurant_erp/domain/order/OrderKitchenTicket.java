package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.enums.KitchenTicketStatus;
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
@Document("order_kitchen_tickets")
public class OrderKitchenTicket extends BaseEntity {

    private String ticketNumber;

    private KitchenTicketStatus status;

    private String kitchenStation;

    private LocalDateTime sentAt;

    private LocalDateTime startedAt;

    private LocalDateTime readyAt;

    private String note;


    @DBRef
    private Order order;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}