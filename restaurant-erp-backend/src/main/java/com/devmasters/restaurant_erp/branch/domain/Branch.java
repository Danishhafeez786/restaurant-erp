package com.devmasters.restaurant_erp.branch.domain;

import com.devmasters.restaurant_erp.common.domain.Address;
import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import com.devmasters.restaurant_erp.common.domain.ContactInfo;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "branches")
public class Branch extends BaseDomain {

    private String organizationId;

    private String branchName;

    private Address address;

    private ContactInfo contact;

    private String status;
}