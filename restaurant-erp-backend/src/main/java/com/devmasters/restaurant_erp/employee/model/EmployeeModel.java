package com.devmasters.restaurant_erp.employee.model;

import com.devmasters.restaurant_erp.common.enums.EmploymentStatus;
import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.role.model.RoleModel;
import com.devmasters.restaurant_erp.auth.model.UserModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "CNIC is required")
    @Pattern(
            regexp = "^[0-9]{5}-[0-9]{7}-[0-9]$",
            message = "CNIC must be in the format 12345-1234567-1"
    )
    private String cnic;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+\\-() ]{7,20}$",
            message = "Invalid phone number"
    )
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "Emergency contact is required")
    @Pattern(
            regexp = "^[0-9+\\-() ]{7,20}$",
            message = "Invalid emergency contact number"
    )
    private String emergencyContact;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be in the future")
    private LocalDate joiningDate;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    private BigDecimal salary;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus employmentStatus;

    @Valid
    @NotNull(message = "Role is required")
    private RoleModel roleModel;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    @Valid
    @NotNull(message = "Branch is required")
    private BranchModel branchModel;

    @Valid
    @NotNull(message = "User is required")
    private UserModel userModel;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
