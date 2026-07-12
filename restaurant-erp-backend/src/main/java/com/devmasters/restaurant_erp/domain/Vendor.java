package com.devmasters.restaurant_erp.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("expense_vendors")
public class Vendor extends BaseEntity {

    @Indexed
    private String vendorName;

    @Indexed
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

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}