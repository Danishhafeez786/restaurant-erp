package com.devmasters.restaurant_erp.menu.handler;

import com.devmasters.restaurant_erp.menu.domain.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.menu.model.MenuItemModifierGroupModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemModifierGroupSearchCriteria;
import com.devmasters.restaurant_erp.menu.service.MenuItemModifierGroupService;
import com.devmasters.restaurant_erp.menu.transformer.MenuItemModifierGroupTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MenuItemModifierGroupHandler {

    private final MenuItemModifierGroupService service;
    private final MenuItemModifierGroupTransformer transformer;

    public MenuItemModifierGroupModel create(MenuItemModifierGroupModel model) {

        UUID menuItemId = model.getMenuItemModel().getId();
        UUID modifierGroupId = model.getModifierGroupModel().getId();

        if(service.existsByMenuItem_IdAndModifierGroup_Id(menuItemId, modifierGroupId)) {
            throw new RuntimeException(
                    "Modifier Group already assigned to this Menu Item.");
        }

        MenuItemModifierGroup entity = transformer.toEntity(model);
        MenuItemModifierGroup saved = service.create(entity);
        return transformer.toModel(saved);
    }



    public PageResponse<MenuItemModifierGroupModel> getAll(MenuItemModifierGroupSearchCriteria criteria, Pageable pageable) {

        Page<MenuItemModifierGroup> page = service.search(criteria,pageable);
        return PageResponse
                .<MenuItemModifierGroupModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }



    public MenuItemModifierGroupModel update(UUID id, MenuItemModifierGroupModel model) {
        MenuItemModifierGroup entity = transformer.toEntity(model);
        MenuItemModifierGroup updated =
                service.update(
                        id,
                        entity);
        return transformer.toModel(updated);
    }

    public MenuItemModifierGroupModel delete(UUID id) {

        MenuItemModifierGroup deleted = service.delete(id);
        return transformer.toModel(deleted);
    }



    public MenuItemModifierGroupModel restore(UUID id) {
        MenuItemModifierGroup restored = service.restore(id);
        return transformer.toModel(restored);
    }
}
