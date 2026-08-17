package com.devmasters.restaurant_erp.menu.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.menu.domain.MenuItem;
import com.devmasters.restaurant_erp.menu.model.MenuItemModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MenuItemTransformer extends Transformer<MenuItem, MenuItemModel> {

    private final CategoryTransformer categoryTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public MenuItem toEntity(MenuItemModel model) {

        if (model == null)
            return null;

        return MenuItem.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .name(model.getName())
                .code(model.getCode())
                .itemType(model.getItemType())
                .shortDescription(model.getShortDescription())
                .description(model.getDescription())
                .imageUrl(model.getImageUrl())
                .taxGroupId(model.getTaxGroupId())
                .kitchenStationId(model.getKitchenStationId())
                .hasVariants(model.getHasVariants())
                .hasModifiers(model.getHasModifiers())
                .hasAddons(model.getHasAddons())
                .inventoryTracked(model.getInventoryTracked())
                .featured(model.getFeatured())
                .popular(model.getPopular())
                .dineIn(model.getDineIn())
                .takeaway(model.getTakeaway())
                .delivery(model.getDelivery())
                .displayOrder(model.getDisplayOrder())
                .availabilityStatus(model.getAvailabilityStatus())
                .category(categoryTransformer.toEntity(model.getCategoryModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public MenuItemModel toModel(MenuItem entity) {

        if (entity == null)
            return null;

        return MenuItemModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .itemType(entity.getItemType())
                .shortDescription(entity.getShortDescription())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .taxGroupId(entity.getTaxGroupId())
                .kitchenStationId(entity.getKitchenStationId())
                .hasVariants(entity.getHasVariants())
                .hasModifiers(entity.getHasModifiers())
                .hasAddons(entity.getHasAddons())
                .inventoryTracked(entity.getInventoryTracked())
                .featured(entity.getFeatured())
                .popular(entity.getPopular())
                .dineIn(entity.getDineIn())
                .takeaway(entity.getTakeaway())
                .delivery(entity.getDelivery())
                .displayOrder(entity.getDisplayOrder())
                .availabilityStatus(entity.getAvailabilityStatus())
                .categoryModel(categoryTransformer.toModel(entity.getCategory()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
