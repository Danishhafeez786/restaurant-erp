package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Menu.Modifier;
import com.devmasters.restaurant_erp.model.Menu.ModifierModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ModifierTransformer extends Transformer<Modifier, ModifierModel> {

    private final ModifierGroupTransformer modifierGroupTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public Modifier toEntity(ModifierModel model) {

        if(model == null)
            return null;

        return Modifier.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .name(model.getName())
                .code(model.getCode())
                .sku(model.getSku())
                .price(model.getPrice())
                .costPrice(model.getCostPrice())
                .calories(model.getCalories())
                .displayOrder(model.getDisplayOrder())
                .inventoryTracked(model.getInventoryTracked())
                .available(model.getAvailable())
                .modifierGroup(modifierGroupTransformer.toEntity(model.getModifierGroupModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public ModifierModel toModel(Modifier entity) {

        if(entity == null)
            return null;

        return ModifierModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .sku(entity.getSku())
                .price(entity.getPrice())
                .costPrice(entity.getCostPrice())
                .calories(entity.getCalories())
                .displayOrder(entity.getDisplayOrder())
                .inventoryTracked(entity.getInventoryTracked())
                .available(entity.getAvailable())
                .modifierGroupModel(modifierGroupTransformer.toModel(entity.getModifierGroup()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
