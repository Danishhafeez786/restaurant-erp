package com.devmasters.restaurant_erp.model.empoyee;

import com.devmasters.restaurant_erp.enums.EmploymentStatus;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSearchCriteria {

    private String employeeCode;

    private String fullName;

    private String phone;

    private String cnic;

    private UUID roleId;

    private EmploymentStatus employmentStatus;

    private UUID organizationId;

    private UUID branchId;

    private Boolean isActive;
}
