package com.devmasters.restaurant_erp.model.employee;

import com.devmasters.restaurant_erp.enums.EmploymentStatus;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.RoleModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestModel {

    private String cnic;

    private String address;

    private String designation;

    private String emergencyContact;

    private EmploymentStatus employmentStatus;

    private LocalDate joiningDate;

    private Double salary;

    private String email;

    private String fullName;

    private String phone;
    private String referredBy;

    private String password;
    private RoleModel role;

    private OrganizationModel organizationModel;
    private BranchModel branchModel;

    private String referralCode;
}
