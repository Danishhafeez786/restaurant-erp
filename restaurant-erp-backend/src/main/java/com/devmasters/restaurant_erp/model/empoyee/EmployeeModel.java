package com.devmasters.restaurant_erp.model.empoyee;

import com.devmasters.restaurant_erp.enums.EmploymentStatus;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.RoleModel;
import com.devmasters.restaurant_erp.model.UserModel;
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
    private String emergencyContact;
    private LocalDate joiningDate;
    private Double salary;
    private EmploymentStatus employmentStatus;
    private RoleModel roleModel;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private UserModel userModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
