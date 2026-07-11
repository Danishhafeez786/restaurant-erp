package com.devmasters.restaurant_erp.domain.Menu;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.enums.AvailabilityStatus;
import com.devmasters.restaurant_erp.enums.MenuItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("menu_items")
public class MenuItem extends BaseEntity {

    @Indexed
    private String name;

    @Indexed(unique = true)
    private String code;

    private MenuItemType itemType;

    private String shortDescription;

    private String description;

    private String imageUrl;

    private UUID taxGroupId;

    private UUID kitchenStationId;

    private Boolean hasVariants;

    private Boolean hasModifiers;

    private Boolean hasAddons;

    private Boolean inventoryTracked;

    private Boolean featured;

    private Boolean popular;

    private Boolean dineIn;

    private Boolean takeaway;

    private Boolean delivery;

    private Integer displayOrder;

    private AvailabilityStatus availabilityStatus;

    @DBRef
    private Category category;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}
