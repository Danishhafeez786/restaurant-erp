package com.devmasters.restaurant_erp.domain.Menu;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("modifiers")
public class Modifier extends BaseEntity {


    @Indexed
    private String name;

    @Indexed
    private String code;

    private String sku;

    private BigDecimal price;

    private BigDecimal costPrice;

    private Integer calories;

    private Integer displayOrder;

    private Boolean inventoryTracked;

    private Boolean available;

    @DBRef
    private ModifierGroup modifierGroup;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}