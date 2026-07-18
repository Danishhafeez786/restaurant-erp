package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.Gender;
import com.devmasters.restaurant_erp.enums.MemberShipLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class CustomerModel {

    private UUID id;

    @NotBlank(message = "Customer code is required")
    private String customerCode;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+\\-() ]{7,20}$",
            message = "Invalid phone number"
    )
    private String phone;

    @Email(message = "Invalid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @PositiveOrZero(message = "Loyalty points cannot be negative")
    private Integer loyaltyPoints;

    @DecimalMin(value = "0.0", inclusive = true, message = "Credit balance cannot be negative")
    private Double creditBalance;

    @Valid
    @NotNull(message = "Branch is required")
    private BranchModel branchModel;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private Gender gender;

    @PositiveOrZero(message = "Total orders cannot be negative")
    private Integer totalOrders;

    @DecimalMin(value = "0.0", inclusive = true, message = "Total spent cannot be negative")
    private Double totalSpent;

    private LocalDate lastOrderDate;

    @NotNull(message = "Membership level is required")
    private MemberShipLevel membershipLevel;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
