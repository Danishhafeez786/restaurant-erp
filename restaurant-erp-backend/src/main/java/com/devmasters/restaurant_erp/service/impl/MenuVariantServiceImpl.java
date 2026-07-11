package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.Menu.MenuVariant;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuVariantSearchCriteria;
import com.devmasters.restaurant_erp.repository.MenuVariantRepository;
import com.devmasters.restaurant_erp.service.MenuVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuVariantServiceImpl implements MenuVariantService {

    private final MenuVariantRepository menuVariantRepository;

    @Override
    public boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId) {
        return menuVariantRepository
                .existsByCodeIgnoreCaseAndBranch_Id(
                        code,
                        branchId);
    }

    @Override
    public boolean existsBySkuIgnoreCaseAndBranch_Id(String sku, UUID branchId) {
        return menuVariantRepository
                .existsBySkuIgnoreCaseAndBranch_Id(
                        sku,
                        branchId);
    }

    @Override
    public boolean existsByBarcodeAndBranch_Id(String barcode, UUID branchId) {
        return menuVariantRepository
                .existsByBarcodeAndBranch_Id(
                        barcode,
                        branchId);
    }

    @Override
    public boolean existsByNameIgnoreCaseAndMenuItem_Id(String name, UUID menuItemId) {
        return menuVariantRepository
                .existsByNameIgnoreCaseAndMenuItem_Id(
                        name,
                        menuItemId);
    }

    @Override
    public MenuVariant create(MenuVariant entity) {
        return menuVariantRepository.save(entity);
    }

    @Override
    public Page<MenuVariant> search(MenuVariantSearchCriteria criteria, Pageable pageable) {
        return menuVariantRepository.search(
                criteria,
                pageable);
    }

    @Override
    public MenuVariant findById(UUID id) {
        return menuVariantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Menu Variant not found."));
    }

    @Override
    public MenuVariant update(UUID id, MenuVariant entity) {

        MenuVariant existing = findById(id);
        existing.setName(entity.getName());
        existing.setCode(entity.getCode());
        existing.setSku(entity.getSku());
        existing.setBarcode(entity.getBarcode());
        existing.setSellingPrice(entity.getSellingPrice());
        existing.setCostPrice(entity.getCostPrice());
        existing.setPreparationTime(entity.getPreparationTime());
        existing.setCalories(entity.getCalories());
        existing.setWeight(entity.getWeight());
        existing.setUnit(entity.getUnit());
        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setDefaultVariant(entity.getDefaultVariant());
        existing.setInventoryTracked(entity.getInventoryTracked());
        existing.setAvailabilityStatus(entity.getAvailabilityStatus());
        existing.setMenuItem(entity.getMenuItem());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());
        return menuVariantRepository.save(existing);
    }

    @Override
    public MenuVariant delete(UUID id) {

        MenuVariant variant = findById(id);
        if (!Boolean.TRUE.equals(variant.getIsActive())) {
            throw new RuntimeException(
                    "Menu Variant already deleted.");
        }

        variant.setIsActive(false);
        return menuVariantRepository.save(variant);
    }

    @Override
    public MenuVariant restore(UUID id) {

        MenuVariant variant = findById(id);
        variant.setIsActive(true);
        return menuVariantRepository.save(variant);
    }
}
