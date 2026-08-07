package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Menu.ModifierGroup;
import com.devmasters.restaurant_erp.model.Menu.ModifierGroupModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierGroupSearchCriteria;
import com.devmasters.restaurant_erp.service.ModifierGroupService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.ModifierGroupTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ModifierGroupHandler {

    private final ModifierGroupService modifierGroupService;
    private final ModifierGroupTransformer modifierGroupTransformer;
    private final CodeGeneratorService codeGeneratorService;


    public ModifierGroupModel create(ModifierGroupModel model) {

        UUID branchId = model.getBranchModel().getId();
        if(model.getCode() == null || model.getCode().isBlank()) {
            model.setCode(codeGeneratorService
                            .generateModifierGroupCode(
                                    branchId)
            );
        }

        if(modifierGroupService.existsByCodeIgnoreCaseAndBranch_Id(model.getCode(), branchId)) {
            throw new RuntimeException("Modifier Group Code already exists : " + model.getCode());
        }

        if(modifierGroupService.existsByNameIgnoreCaseAndBranch_Id(model.getName(), branchId)) {
            throw new RuntimeException(
                    "Modifier Group already exists : "
                            + model.getName());
        }

        ModifierGroup entity = modifierGroupTransformer.toEntity(model);
        ModifierGroup saved = modifierGroupService.create(entity);
        return modifierGroupTransformer.toModel(saved);
    }

    public PageResponse<ModifierGroupModel> getAll(ModifierGroupSearchCriteria criteria, Pageable pageable) {

        Page<ModifierGroup> page = modifierGroupService.search(criteria,pageable);
        return PageResponse.<ModifierGroupModel>builder()
                .content(modifierGroupTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages()).page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public ModifierGroupModel update(UUID id, ModifierGroupModel model) {

        ModifierGroup entity = modifierGroupTransformer.toEntity(model);
        ModifierGroup updated = modifierGroupService.update(id,entity);
        return modifierGroupTransformer.toModel(updated);
    }

    public ModifierGroupModel delete(UUID id) {

        ModifierGroup deleted = modifierGroupService.delete(id);
        return modifierGroupTransformer
                .toModel(deleted);
    }

    public ModifierGroupModel restore(UUID id) {

        ModifierGroup restored = modifierGroupService.restore(id);
        return modifierGroupTransformer
                .toModel(restored);
    }
}
