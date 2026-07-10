package com.devmasters.restaurant_erp.domain;

import com.devmasters.restaurant_erp.enums.TableStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("tables")
public class RestaurantTable extends BaseEntity {

    private String tableNumber;
    private String tableName;
    private Integer capacity;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;

    @DBRef
    private Floor floor;

    private TableStatus status;

    @DBRef
    private Order currentOrder;

    @DBRef
    private Customer currentCustomer;

    @DBRef
    private Employee assignedWaiter;

    private LocalDateTime occupiedAt;
    private String qrCode;

    @Builder.Default
    private Boolean reservable = true;

    @Builder.Default
    private Boolean merged = false;

    private String notes;
}
