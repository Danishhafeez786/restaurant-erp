package com.devmasters.restaurant_erp.customer.domain;

import com.devmasters.restaurant_erp.common.domain.Address;
import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import com.devmasters.restaurant_erp.common.domain.ContactInfo;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "customers")
public class Customer extends BaseDomain {

    private String name;

    private ContactInfo contact;

    private Address address;

    private Integer loyaltyPoints;

    private Integer totalOrders;
}
