package com.devmasters.restaurant_erp.customer.domain;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.common.enums.Gender;
import com.devmasters.restaurant_erp.common.enums.MemberShipLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer extends BaseEntity {
    private String customerCode;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private Integer loyaltyPoints = 0;
    private Double creditBalance = 0.0;
    @DBRef
    private Branch branch;

    private LocalDate dateOfBirth;
    private Gender gender;
    private Integer totalOrders;
    private Double totalSpent;
    private LocalDate lastOrderDate;
    private MemberShipLevel membershipLevel; // Silver, Gold, Platinum
}