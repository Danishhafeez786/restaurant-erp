package com.devmasters.restaurant_erp.tablemanagment.domain;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.customer.domain.Customer;
import com.devmasters.restaurant_erp.floor.domain.Floor;
import com.devmasters.restaurant_erp.order.domain.Order;
import com.devmasters.restaurant_erp.common.enums.TableStatus;
import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.organization.domain.Organization;
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
