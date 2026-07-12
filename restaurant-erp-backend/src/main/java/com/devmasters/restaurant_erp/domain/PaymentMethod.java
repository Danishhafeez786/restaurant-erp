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
@Document("payment_methods")
public class PaymentMethod extends BaseEntity {

    @Indexed
    private String methodName;

    @Indexed
    private String code;

    private String description;

    private Boolean online;

    private Boolean cashBased;

    @DBRef
    private Organization organization;
}