package com.devmasters.restaurant_erp.customer.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.MemberShipLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSearchCriteria {

    private String customerCode;

    private String fullName;

    private String phone;

    private String email;

    private UUID branchId;

    private MemberShipLevel membershipLevel;

    private String gender;

    private Boolean isActive;
}
