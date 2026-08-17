package com.devmasters.restaurant_erp.menu.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.menu.domain.ModifierGroup;
import com.devmasters.restaurant_erp.menu.model.ModifierGroupModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ModifierGroupTransformer extends Transformer<ModifierGroup, ModifierGroupModel> {

    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public ModifierGroup toEntity(ModifierGroupModel model) {

        if (model == null)
            return null;

        return ModifierGroup.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .name(model.getName())
                .code(model.getCode())
                .description(model.getDescription())
                .minimumSelection(model.getMinimumSelection())
                .maximumSelection(model.getMaximumSelection())
                .required(model.getRequired())
                .displayOrder(model.getDisplayOrder())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public ModifierGroupModel toModel(ModifierGroup entity) {

        if (entity == null)
            return null;

        return ModifierGroupModel.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .minimumSelection(entity.getMinimumSelection())
                .maximumSelection(entity.getMaximumSelection())
                .required(entity.getRequired())
                .displayOrder(entity.getDisplayOrder())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
