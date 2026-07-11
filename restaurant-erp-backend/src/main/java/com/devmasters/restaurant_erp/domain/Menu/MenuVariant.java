package com.devmasters.restaurant_erp.domain.Menu;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.enums.AvailabilityStatus;
import com.devmasters.restaurant_erp.enums.Unit;
import com.devmasters.restaurant_erp.enums.WeightUnit;
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
@Document("menu_variants")
public class MenuVariant extends BaseEntity {

    private String name;

    @Indexed
    private String code;

    @Indexed
    private String sku;

    private String barcode;

    private BigDecimal sellingPrice;

    private BigDecimal costPrice;

    private Integer preparationTime;

    private Integer calories;

    private Double weight;

    private Unit unit;

    private Integer displayOrder;

    private Boolean defaultVariant;

    private Boolean inventoryTracked;

    private AvailabilityStatus availabilityStatus;

    @DBRef
    private MenuItem menuItem;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}
