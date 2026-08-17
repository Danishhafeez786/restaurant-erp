package com.devmasters.restaurant_erp.menu.service;

import com.devmasters.restaurant_erp.menu.domain.ModifierGroup;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.ModifierGroupSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ModifierGroupService {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);

    boolean existsByNameIgnoreCaseAndBranch_Id(String name, UUID branchId);

    ModifierGroup create(ModifierGroup entity);

    Page<ModifierGroup> search(ModifierGroupSearchCriteria criteria, Pageable pageable);

    ModifierGroup findById(UUID id);

    ModifierGroup update(UUID id, ModifierGroup entity);

    ModifierGroup delete(UUID id);

    ModifierGroup restore(UUID id);
}
