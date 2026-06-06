package com.devmasters.restaurant_erp.employee.domain;

import com.devmasters.restaurant_erp.common.domain.Address;
import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "employees")
public class Employee extends BaseDomain {

    private String organizationId;

    private String branchId;

    private String userId;

    private String designation;

    private String cnic;

    private LocalDate joiningDate;

    private BigDecimal salary;

    private Address address;
}
