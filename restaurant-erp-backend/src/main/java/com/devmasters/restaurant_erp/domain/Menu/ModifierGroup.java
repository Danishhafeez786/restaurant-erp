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
@Document("modifier_groups")
public class ModifierGroup extends BaseEntity {

    @Indexed
    private String name;

    @Indexed(unique = true)
    private String code;

    private String description;

    private Integer minimumSelection;

    private Integer maximumSelection;

    private Boolean required;

    private Integer displayOrder;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}
