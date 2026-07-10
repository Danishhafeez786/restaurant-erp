package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTableSearchCriteria {

    private String tableNumber;

    private String tableName;

    private UUID organizationId;

    private UUID branchId;

    private UUID floorId;

    private UUID currentCustomerId;

    private UUID assignedWaiterId;

    private TableStatus status;

    private Integer capacity;

    private Boolean reservable;

    private Boolean merged;

    private Boolean isActive;
}