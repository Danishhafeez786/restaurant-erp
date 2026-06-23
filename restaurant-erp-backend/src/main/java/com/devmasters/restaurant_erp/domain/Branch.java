package com.devmasters.restaurant_erp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("branches")
public class Branch extends BaseEntity {
    private String branchName;
    private String branchCode;
    private String address;
    private String city;
    private String phone;
    @DBRef
    private Organization organization;
}
