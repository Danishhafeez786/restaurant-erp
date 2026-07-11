package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Menu.MenuVariant;
import com.devmasters.restaurant_erp.model.Menu.MenuVariantModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MenuVariantTransformer extends Transformer<MenuVariant, MenuVariantModel> {

    private final MenuItemTransformer menuItemTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public MenuVariant toEntity(MenuVariantModel model) {

        if (model == null)
            return null;

        return MenuVariant.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .name(model.getName())
                .code(model.getCode())
                .sku(model.getSku())
                .barcode(model.getBarcode())
                .sellingPrice(model.getSellingPrice())
                .costPrice(model.getCostPrice())
                .preparationTime(model.getPreparationTime())
                .calories(model.getCalories())
                .weight(model.getWeight())
                .unit(model.getUnit())
                .displayOrder(model.getDisplayOrder())
                .defaultVariant(model.getDefaultVariant())
                .inventoryTracked(model.getInventoryTracked())
                .availabilityStatus(model.getAvailabilityStatus())
                .menuItem(menuItemTransformer.toEntity(model.getMenuItemModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public MenuVariantModel toModel(MenuVariant entity) {

        if (entity == null)
            return null;

        return MenuVariantModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .sku(entity.getSku())
                .barcode(entity.getBarcode())
                .sellingPrice(entity.getSellingPrice())
                .costPrice(entity.getCostPrice())
                .preparationTime(entity.getPreparationTime())
                .calories(entity.getCalories())
                .weight(entity.getWeight())
                .unit(entity.getUnit())
                .displayOrder(entity.getDisplayOrder())
                .defaultVariant(entity.getDefaultVariant())
                .inventoryTracked(entity.getInventoryTracked())
                .availabilityStatus(entity.getAvailabilityStatus())
                .menuItemModel(menuItemTransformer.toModel(entity.getMenuItem()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
