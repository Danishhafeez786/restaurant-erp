package com.devmasters.restaurant_erp.menu.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.menu.domain.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.menu.model.MenuItemModifierGroupModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MenuItemModifierGroupTransformer extends Transformer<MenuItemModifierGroup, MenuItemModifierGroupModel> {

    private final MenuItemTransformer menuItemTransformer;
    private final ModifierGroupTransformer modifierGroupTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public MenuItemModifierGroup toEntity(MenuItemModifierGroupModel model) {

        if(model == null)
            return null;

        return MenuItemModifierGroup.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .menuItem(menuItemTransformer.toEntity(model.getMenuItemModel()))
                .modifierGroup(modifierGroupTransformer.toEntity(model.getModifierGroupModel()))
                .displayOrder(model.getDisplayOrder())
                .required(model.getRequired())
                .minimumSelection(model.getMinimumSelection())
                .maximumSelection(model.getMaximumSelection())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }


    @Override
    public MenuItemModifierGroupModel toModel(MenuItemModifierGroup entity) {

        if(entity == null)
            return null;


        return MenuItemModifierGroupModel.builder()
                .id(entity.getId())
                .menuItemModel(menuItemTransformer.toModel(entity.getMenuItem()))
                .modifierGroupModel(modifierGroupTransformer.toModel(entity.getModifierGroup()))
                .displayOrder(entity.getDisplayOrder())
                .required(entity.getRequired())
                .minimumSelection(entity.getMinimumSelection())
                .maximumSelection(entity.getMaximumSelection())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
