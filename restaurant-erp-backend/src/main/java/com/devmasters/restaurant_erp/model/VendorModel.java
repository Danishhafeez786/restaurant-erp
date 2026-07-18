package com.devmasters.restaurant_erp.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorModel {

    private UUID id;

    @NotBlank(message = "Vendor name is required")
    @Size(min = 2, max = 100, message = "Vendor name must be between 2 and 100 characters")
    private String vendorName;

    @NotBlank(message = "Vendor code is required")
    private String vendorCode;

    @NotBlank(message = "Contact person is required")
    @Size(max = 100, message = "Contact person cannot exceed 100 characters")
    private String contactPerson;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+\\-() ]{7,20}$",
            message = "Invalid phone number"
    )
    private String phone;

    @Pattern(
            regexp = "^$|^[0-9+\\-() ]{7,20}$",
            message = "Invalid alternate phone number"
    )
    private String alternatePhone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 255, message = "Website cannot exceed 255 characters")
    private String website;

    @Size(max = 50, message = "Tax number cannot exceed 50 characters")
    private String taxNumber;

    @Size(max = 50, message = "Registration number cannot exceed 50 characters")
    private String registrationNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Size(max = 20, message = "Zip code cannot exceed 20 characters")
    private String zipCode;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    @Valid
    @NotNull(message = "Branch is required")
    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}