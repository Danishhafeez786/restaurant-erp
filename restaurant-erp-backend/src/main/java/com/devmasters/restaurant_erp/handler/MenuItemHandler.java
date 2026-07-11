package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Menu.MenuItem;
import com.devmasters.restaurant_erp.model.Menu.MenuItemModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuItemSearchCriteria;
import com.devmasters.restaurant_erp.service.MenuItemService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.MenuItemTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MenuItemHandler {

    private final MenuItemService menuItemService;
    private final MenuItemTransformer menuItemTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public MenuItemModel create(MenuItemModel model) {

        UUID branchId = model.getBranchModel().getId();

        if (menuItemService.existsByNameIgnoreCaseAndBranch_Id(model.getName(), branchId)) {
            throw new RuntimeException(
                    "Menu Item Name already exists : "
                            + model.getName());
        }

        MenuItem entity = menuItemTransformer.toEntity(model);
        entity.setCode(codeGeneratorService.generateMenuItemCode(entity.getItemType()));
        MenuItem saved = menuItemService.create(entity);
        return menuItemTransformer.toModel(saved);
    }

    public PageResponse<MenuItemModel> getAll(MenuItemSearchCriteria criteria, Pageable pageable) {

        Page<MenuItem> page = menuItemService.search(criteria, pageable);
        return PageResponse.<MenuItemModel>builder()
                .content(menuItemTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public MenuItemModel update(UUID id, MenuItemModel model) {

        MenuItem entity = menuItemTransformer.toEntity(model);
        MenuItem updated = menuItemService.update(id, entity);
        return menuItemTransformer.toModel(updated);
    }

    public MenuItemModel delete(UUID id) {

        MenuItem deleted = menuItemService.delete(id);
        return menuItemTransformer.toModel(deleted);
    }

    public MenuItemModel restore(UUID id) {

        MenuItem restored = menuItemService.restore(id);
        return menuItemTransformer.toModel(restored);
    }
}