package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Menu.ModifierGroup;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierGroupSearchCriteria;
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
