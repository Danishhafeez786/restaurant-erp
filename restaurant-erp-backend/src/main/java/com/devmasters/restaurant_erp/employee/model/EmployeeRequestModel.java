package com.devmasters.restaurant_erp.employee.model;

import com.devmasters.restaurant_erp.common.enums.EmploymentStatus;
import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.role.model.RoleModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "CNIC is required")
    @Pattern(
            regexp = "^\\d{5}-\\d{7}-\\d{1}$",
            message = "CNIC must be in the format 12345-1234567-1"
    )
    private String cnic;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "Designation is required")
    @Size(min = 2, max = 100, message = "Designation must be between 2 and 100 characters")
    private String designation;

    @NotBlank(message = "Emergency contact is required")
    @Pattern(
            regexp = "^[0-9+\\-() ]{7,20}$",
            message = "Invalid emergency contact number"
    )
    private String emergencyContact;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    private Double salary;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+\\-() ]{7,20}$",
            message = "Invalid phone number"
    )
    private String phone;

    @Size(max = 100, message = "Referred by cannot exceed 100 characters")
    private String referredBy;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @Valid
    @NotNull(message = "Role is required")
    private RoleModel role;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    @Valid
    @NotNull(message = "Branch is required")
    private BranchModel branchModel;

    @Size(max = 50, message = "Referral code cannot exceed 50 characters")
    private String referralCode;
}
