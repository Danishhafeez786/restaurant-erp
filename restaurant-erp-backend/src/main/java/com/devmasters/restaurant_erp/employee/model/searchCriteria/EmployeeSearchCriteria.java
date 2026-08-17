package com.devmasters.restaurant_erp.employee.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.EmploymentStatus;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSearchCriteria {

    private String search;

    private UUID roleId;

    private EmploymentStatus employmentStatus;

    private UUID organizationId;

    private UUID branchId;

    private Boolean isActive;
}
