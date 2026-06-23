package com.devmasters.restaurant_erp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private LocalDate joiningDate;
    private Double salary;
    @DBRef
    private Organization organization;
    @DBRef
    private Branch branch;
    @DBRef
    private User user;
}