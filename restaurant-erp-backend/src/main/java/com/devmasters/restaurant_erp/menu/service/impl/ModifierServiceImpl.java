package com.devmasters.restaurant_erp.menu.service.impl;

import com.devmasters.restaurant_erp.menu.domain.Modifier;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.ModifierSearchCriteria;
import com.devmasters.restaurant_erp.menu.respository.ModifierRepository;
import com.devmasters.restaurant_erp.menu.service.ModifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModifierServiceImpl implements ModifierService {

    private final ModifierRepository modifierRepository;

    @Override
    public boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId) {
        return modifierRepository
                .existsByCodeIgnoreCaseAndBranch_Id(
                        code,
                        branchId);
    }

    @Override
    public boolean existsByNameIgnoreCaseAndModifierGroup_Id(String name, UUID modifierGroupId) {
        return modifierRepository
                .existsByNameIgnoreCaseAndModifierGroup_Id(
                        name,
                        modifierGroupId);
    }

    @Override
    public boolean existsBySkuIgnoreCaseAndBranch_Id(String sku, UUID branchId) {
        return modifierRepository
                .existsBySkuIgnoreCaseAndBranch_Id(
                        sku,
                        branchId);
    }

    @Override
    public Modifier create(Modifier entity) {
        return modifierRepository.save(entity);
    }

    @Override
    public Page<Modifier> search(ModifierSearchCriteria criteria, Pageable pageable) {
        return modifierRepository.search(
                criteria,
                pageable);
    }

    @Override
    public Modifier findById(UUID id) {
        return modifierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Modifier not found."));
    }

    @Override
    public Modifier update(UUID id, Modifier entity) {

        Modifier existing = findById(id);
        existing.setName(entity.getName());
        existing.setSku(entity.getSku());
        existing.setPrice(entity.getPrice());
        existing.setCostPrice(entity.getCostPrice());
        existing.setCalories(entity.getCalories());
        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setInventoryTracked(entity.getInventoryTracked());
        existing.setAvailable(entity.getAvailable());
        existing.setModifierGroup(entity.getModifierGroup());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());
        return modifierRepository.save(existing);
    }

    @Override
    public Modifier delete(UUID id) {

        Modifier modifier = findById(id);
        if(!Boolean.TRUE.equals(modifier.getIsActive())) {
            throw new RuntimeException(
                    "Modifier already deleted.");
        }
        modifier.setIsActive(false);
        return modifierRepository.save(modifier);
    }

    @Override
    public Modifier restore(UUID id) {
        Modifier modifier = findById(id);
        modifier.setIsActive(true);
        return modifierRepository.save(modifier);
    }
}
