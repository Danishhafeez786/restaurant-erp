package com.devmasters.restaurant_erp.menu.service;

import com.devmasters.restaurant_erp.menu.domain.Modifier;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.ModifierSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ModifierService {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);

    boolean existsByNameIgnoreCaseAndModifierGroup_Id(String name, UUID modifierGroupId);

    boolean existsBySkuIgnoreCaseAndBranch_Id(String sku, UUID branchId);

    Modifier create(Modifier entity);

    Page<Modifier> search(ModifierSearchCriteria criteria, Pageable pageable);

    Modifier findById(UUID id);

    Modifier update(UUID id, Modifier entity);

    Modifier delete(UUID id);

    Modifier restore(UUID id);
}