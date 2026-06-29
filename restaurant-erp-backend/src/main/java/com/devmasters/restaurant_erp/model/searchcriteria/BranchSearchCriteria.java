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
public class BranchSearchCriteria {

    private String branchName;

    private String branchCode;

    private String city;

    private String phone;

    private Boolean isActive;

    private UUID organizationId;
}