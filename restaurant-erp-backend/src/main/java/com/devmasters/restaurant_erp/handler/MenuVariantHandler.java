package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Menu.MenuVariant;
import com.devmasters.restaurant_erp.model.Menu.MenuVariantModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuVariantSearchCriteria;
import com.devmasters.restaurant_erp.service.MenuVariantService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.MenuVariantTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MenuVariantHandler {

    private final MenuVariantService menuVariantService;
    private final MenuVariantTransformer menuVariantTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public MenuVariantModel create(MenuVariantModel model) {

        UUID branchId = model.getBranchModel().getId();
        UUID menuItemId = model.getMenuItemModel().getId();

        if (model.getCode() == null || model.getCode().isBlank()) {
            model.setCode(
                    codeGeneratorService.generateMenuVariantCode(branchId)
            );
        }

        if (menuVariantService.existsByCodeIgnoreCaseAndBranch_Id(
                model.getCode(),
                branchId)) {
            throw new RuntimeException(
                    "Menu Variant Code already exists : " + model.getCode()
            );
        }
        if (model.getSku() != null &&
                !model.getSku().isBlank() &&
                menuVariantService.existsBySkuIgnoreCaseAndBranch_Id(
                        model.getSku(),
                        branchId)) {
            throw new RuntimeException(
                    "SKU already exists : " + model.getSku());
        }

        if (model.getBarcode() != null &&
                !model.getBarcode().isBlank() &&
                menuVariantService.existsByBarcodeAndBranch_Id(
                        model.getBarcode(),
                        branchId)) {

            throw new RuntimeException(
                    "Barcode already exists : " + model.getBarcode()
            );
        }

        if (menuVariantService.existsByNameIgnoreCaseAndMenuItem_Id(
                model.getName(),
                menuItemId)) {

            throw new RuntimeException(
                    "Variant already exists : " + model.getName()
            );
        }

        MenuVariant entity = menuVariantTransformer.toEntity(model);
        MenuVariant saved = menuVariantService.create(entity);
        return menuVariantTransformer.toModel(saved);
    }

    public PageResponse<MenuVariantModel> getAll(
            MenuVariantSearchCriteria criteria,
            Pageable pageable) {

        Page<MenuVariant> page =
                menuVariantService.search(criteria, pageable);

        return PageResponse.<MenuVariantModel>builder()
                .content(
                        menuVariantTransformer.toModels(page.getContent())
                )
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public MenuVariantModel update(
            UUID id,
            MenuVariantModel model) {

        MenuVariant entity =
                menuVariantTransformer.toEntity(model);

        MenuVariant updated =
                menuVariantService.update(id, entity);

        return menuVariantTransformer.toModel(updated);
    }

    public MenuVariantModel delete(UUID id) {

        MenuVariant deleted =
                menuVariantService.delete(id);

        return menuVariantTransformer.toModel(deleted);
    }

    public MenuVariantModel restore(UUID id) {

        MenuVariant restored =
                menuVariantService.restore(id);

        return menuVariantTransformer.toModel(restored);
    }
}
