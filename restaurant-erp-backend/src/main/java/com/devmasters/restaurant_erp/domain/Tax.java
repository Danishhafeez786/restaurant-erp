package com.devmasters.restaurant_erp.domain;

import com.devmasters.restaurant_erp.enums.TaxCalculationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("taxes")
public class Tax extends BaseEntity {

    private String taxCode;
    private String taxName;
    private TaxCalculationType calculationType;
    private BigDecimal rate;
    private String description;
    private Boolean defaultTax;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}