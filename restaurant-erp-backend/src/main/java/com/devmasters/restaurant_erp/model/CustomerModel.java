package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.MemberShipLevel;
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
    private String customerCode;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private Integer loyaltyPoints;
    private Double creditBalance;
    private BranchModel branchModel;
    private LocalDate dateOfBirth;
    private String gender;
    private Integer totalOrders;
    private Double totalSpent;
    private LocalDate lastOrderDate;
    private MemberShipLevel membershipLevel; // Silver, Gold, Platinum
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
