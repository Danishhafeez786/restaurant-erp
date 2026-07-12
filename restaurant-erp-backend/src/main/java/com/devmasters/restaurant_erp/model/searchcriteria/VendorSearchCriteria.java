package com.devmasters.restaurant_erp.model.searchcriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorSearchCriteria {

    private String vendorName;

    private String vendorCode;

    private String contactPerson;

    private String phone;

    private String email;

    private String city;

    private String state;

    private String country;

    private UUID organizationId;

    private UUID branchId;

    private Boolean isActive;
}