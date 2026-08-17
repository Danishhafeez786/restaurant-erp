package com.devmasters.restaurant_erp.menu.service.impl;

import com.devmasters.restaurant_erp.menu.domain.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemModifierGroupSearchCriteria;
import com.devmasters.restaurant_erp.menu.respository.MenuItemModifierGroupRepository;
import com.devmasters.restaurant_erp.menu.service.MenuItemModifierGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemModifierGroupServiceImpl implements MenuItemModifierGroupService {

    private final MenuItemModifierGroupRepository repository;

    @Override
    public boolean existsByMenuItem_IdAndModifierGroup_Id(UUID menuItemId, UUID modifierGroupId) {
        return repository
                .existsByMenuItem_IdAndModifierGroup_Id(
                        menuItemId,
                        modifierGroupId);
    }


    @Override
    public MenuItemModifierGroup create(MenuItemModifierGroup entity) {
        return repository.save(entity);
    }


    @Override
    public Page<MenuItemModifierGroup> search(MenuItemModifierGroupSearchCriteria criteria, Pageable pageable) {
        return repository.search(
                criteria,
                pageable);
    }


    @Override
    public MenuItemModifierGroup findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Menu Item Modifier Group not found."));
    }


    @Override
    public MenuItemModifierGroup update(UUID id, MenuItemModifierGroup entity) {
        MenuItemModifierGroup existing = findById(id);

        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setRequired(entity.getRequired());
        existing.setMinimumSelection(entity.getMinimumSelection());
        existing.setMaximumSelection(entity.getMaximumSelection());
        existing.setMenuItem(entity.getMenuItem());
        existing.setModifierGroup(entity.getModifierGroup());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());

        return repository.save(existing);
    }


    @Override
    public MenuItemModifierGroup delete(UUID id) {
        MenuItemModifierGroup entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }


    @Override
    public MenuItemModifierGroup restore(UUID id) {

        MenuItemModifierGroup entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}
