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
public class BranchModel {
    private UUID id;
    private String branchName;
    private String branchCode;
    private String address;
    private String city;
    private String phone;
    private OrganizationModel organizationModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
