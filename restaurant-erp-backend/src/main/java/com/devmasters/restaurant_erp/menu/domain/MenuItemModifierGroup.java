package com.devmasters.restaurant_erp.menu.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.organization.domain.Organization;
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
@Document("menu_item_modifier_groups")
public class MenuItemModifierGroup extends BaseEntity {

    @DBRef
    private MenuItem menuItem;

    @DBRef
    private ModifierGroup modifierGroup;

    private Integer displayOrder;

    private Boolean required;

    private Integer minimumSelection;

    private Integer maximumSelection;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}