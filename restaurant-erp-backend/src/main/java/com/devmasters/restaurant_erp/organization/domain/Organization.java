package com.devmasters.restaurant_erp.organization.domain;


import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "organizations")
public class Organization extends BaseDomain {

    private String name;

    private String ownerId;

    private String subscriptionPlan;

    private String status;
}
