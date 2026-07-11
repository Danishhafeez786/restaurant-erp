package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Menu.Modifier;
import com.devmasters.restaurant_erp.model.Menu.ModifierModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierSearchCriteria;
import com.devmasters.restaurant_erp.service.ModifierService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.ModifierTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ModifierHandler {

    private final ModifierService modifierService;
    private final ModifierTransformer modifierTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public ModifierModel create(ModifierModel model) {

        UUID branchId = model.getBranchModel().getId();
        UUID modifierGroupId = model.getModifierGroupModel().getId();
        if(model.getCode() == null || model.getCode().isBlank()) {
            model.setCode(codeGeneratorService
                            .generateModifierCode(branchId)
            );
        }

        if(modifierService.existsByCodeIgnoreCaseAndBranch_Id(model.getCode(), branchId)) {
            throw new RuntimeException(
                    "Modifier code already exists : "
                            + model.getCode());
        }


        if(model.getSku() != null && !model.getSku().isBlank() &&
                modifierService.existsBySkuIgnoreCaseAndBranch_Id(model.getSku(), branchId)) {
            throw new RuntimeException(
                    "SKU already exists : "
                            + model.getSku());
        }


        if(modifierService.existsByNameIgnoreCaseAndModifierGroup_Id(model.getName(), modifierGroupId)) {
            throw new RuntimeException(
                    "Modifier already exists in this group : "
                            + model.getName());
        }

        Modifier entity = modifierTransformer.toEntity(model);
        Modifier saved = modifierService.create(entity);
        return modifierTransformer.toModel(saved);
    }


    public PageResponse<ModifierModel> getAll(ModifierSearchCriteria criteria, Pageable pageable) {

        Page<Modifier> page = modifierService.search(criteria, pageable);
        return PageResponse.<ModifierModel>builder()
                .content(modifierTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }


    public ModifierModel update(UUID id, ModifierModel model) {

        Modifier entity = modifierTransformer.toEntity(model);
        Modifier updated = modifierService.update(id, entity);
        return modifierTransformer.toModel(updated);
    }


    public ModifierModel delete(UUID id) {

        Modifier deleted = modifierService.delete(id);
        return modifierTransformer.toModel(deleted);
    }


    public ModifierModel restore(UUID id) {

        Modifier restored = modifierService.restore(id);
        return modifierTransformer.toModel(restored);
    }
}
