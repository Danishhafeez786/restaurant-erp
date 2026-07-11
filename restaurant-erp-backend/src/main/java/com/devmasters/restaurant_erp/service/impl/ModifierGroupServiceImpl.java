package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.Menu.ModifierGroup;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierGroupSearchCriteria;
import com.devmasters.restaurant_erp.repository.ModifierGroupRepository;
import com.devmasters.restaurant_erp.service.ModifierGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModifierGroupServiceImpl implements ModifierGroupService {

    private final ModifierGroupRepository modifierGroupRepository;

    @Override
    public boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId) {
        return modifierGroupRepository
                .existsByCodeIgnoreCaseAndBranch_Id(
                        code,
                        branchId);
    }


    @Override
    public boolean existsByNameIgnoreCaseAndBranch_Id(String name, UUID branchId) {
        return modifierGroupRepository
                .existsByNameIgnoreCaseAndBranch_Id(
                        name,
                        branchId);
    }


    @Override
    public ModifierGroup create(ModifierGroup entity) {
        return modifierGroupRepository.save(entity);
    }


    @Override
    public Page<ModifierGroup> search(ModifierGroupSearchCriteria criteria, Pageable pageable) {
        return modifierGroupRepository.search(
                criteria,
                pageable);
    }


    @Override
    public ModifierGroup findById(UUID id) {
        return modifierGroupRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Modifier Group not found."));
    }


    @Override
    public ModifierGroup update(UUID id, ModifierGroup entity) {

        ModifierGroup existing = findById(id);
        existing.setName(entity.getName());

        existing.setDescription(entity.getDescription());
        existing.setMinimumSelection(entity.getMinimumSelection());
        existing.setMaximumSelection(entity.getMaximumSelection());
        existing.setRequired(entity.getRequired());
        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());

        return modifierGroupRepository.save(existing);
    }


    @Override
    public ModifierGroup delete(UUID id) {

        ModifierGroup group = findById(id);
        if (!Boolean.TRUE.equals(group.getIsActive())) {
            throw new RuntimeException(
                    "Modifier Group already deleted.");
        }
        group.setIsActive(false);
        return modifierGroupRepository.save(group);
    }

    @Override
    public ModifierGroup restore(UUID id) {

        ModifierGroup group = findById(id);
        group.setIsActive(true);
        return modifierGroupRepository.save(group);
    }
}
