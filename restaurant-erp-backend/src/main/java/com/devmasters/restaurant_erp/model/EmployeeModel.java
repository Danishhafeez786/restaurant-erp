package com.devmasters.restaurant_erp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeModel {
    private UUID id;
    private String employeeCode;
    private String fullName;
    private String cnic;
    private String phone;
    private String address;
    private LocalDate joiningDate;
    private Double salary;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private UserModel userModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
