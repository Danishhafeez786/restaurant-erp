package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.Menu.MenuItem;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuItemSearchCriteria;
import com.devmasters.restaurant_erp.repository.MenuItemRepository;
import com.devmasters.restaurant_erp.service.MenuItemService;
import com.devmasters.restaurant_erp.transformer.OrganizationTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final OrganizationTransformer organizationTransformer;

    @Override
    public boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId) {
        return menuItemRepository
                .existsByCodeIgnoreCaseAndBranch_Id(
                        code,
                        branchId);
    }

    @Override
    public boolean existsByNameIgnoreCaseAndBranch_Id(
            String name,
            UUID branchId) {

        return menuItemRepository
                .existsByNameIgnoreCaseAndBranch_Id(
                        name,
                        branchId);
    }

    @Override
    public MenuItem create(MenuItem entity) {
        return menuItemRepository.save(entity);
    }

    @Override
    public Page<MenuItem> search(
            MenuItemSearchCriteria criteria,
            Pageable pageable) {

        return menuItemRepository.search(
                criteria,
                pageable);
    }

    @Override
    public MenuItem findById(UUID id) {

        return menuItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Menu Item not found."));
    }

    @Override
    public MenuItem update(UUID id, MenuItem entity) {

        MenuItem existing = findById(id);

        existing.setName(entity.getName());
        existing.setCode(entity.getCode());
        existing.setItemType(entity.getItemType());
        existing.setShortDescription(entity.getShortDescription());
        existing.setDescription(entity.getDescription());
        existing.setImageUrl(entity.getImageUrl());
        existing.setTaxGroupId(entity.getTaxGroupId());
        existing.setKitchenStationId(entity.getKitchenStationId());
        existing.setHasVariants(entity.getHasVariants());
        existing.setHasModifiers(entity.getHasModifiers());
        existing.setHasAddons(entity.getHasAddons());
        existing.setInventoryTracked(entity.getInventoryTracked());
        existing.setFeatured(entity.getFeatured());
        existing.setPopular(entity.getPopular());
        existing.setDineIn(entity.getDineIn());
        existing.setTakeaway(entity.getTakeaway());
        existing.setDelivery(entity.getDelivery());
        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setAvailabilityStatus(entity.getAvailabilityStatus());
        existing.setCategory(entity.getCategory());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());
        return menuItemRepository.save(existing);
    }

    @Override
    public MenuItem delete(UUID id) {

        MenuItem menuItem = findById(id);
        if (!Boolean.TRUE.equals(menuItem.getIsActive())) {
            throw new RuntimeException(
                    "Menu Item already deleted.");
        }
        menuItem.setIsActive(false);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem restore(UUID id) {
        MenuItem menuItem = findById(id);
        menuItem.setIsActive(true);
        return menuItemRepository.save(menuItem);
    }

}