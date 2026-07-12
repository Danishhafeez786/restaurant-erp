package com.devmasters.restaurant_erp.model;

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

    private String vendorName;

    private String vendorCode;

    private String contactPerson;

    private String phone;

    private String alternatePhone;

    private String email;

    private String website;

    private String taxNumber;

    private String registrationNumber;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String notes;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}