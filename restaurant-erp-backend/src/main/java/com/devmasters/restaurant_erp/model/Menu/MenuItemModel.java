package com.devmasters.restaurant_erp.model.Menu;

import com.devmasters.restaurant_erp.enums.AvailabilityStatus;
import com.devmasters.restaurant_erp.enums.MenuItemType;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemModel {

    private UUID id;

    private String name;

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

    private CategoryModel categoryModel;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
