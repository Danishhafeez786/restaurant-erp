package com.devmasters.restaurant_erp.domain;

import com.devmasters.restaurant_erp.enums.EmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("employees")
public class Employee extends BaseEntity {
    private String employeeCode;
    private String fullName;
    private String cnic;
    private String phone;
    private String address;
    private String emergencyContact;
    private LocalDate joiningDate;
    private BigDecimal salary;
    private EmploymentStatus employmentStatus;
    @DBRef
    private Role role;
    @DBRef
    private Organization organization;
    @DBRef
    private Branch branch;
    @DBRef
    private User user;
}