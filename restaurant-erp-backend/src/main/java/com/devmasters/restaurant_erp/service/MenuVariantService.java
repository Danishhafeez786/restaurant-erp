package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Menu.MenuVariant;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuVariantSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuVariantService {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);

    boolean existsBySkuIgnoreCaseAndBranch_Id(String sku, UUID branchId);

    boolean existsByBarcodeAndBranch_Id(String barcode, UUID branchId);

    boolean existsByNameIgnoreCaseAndMenuItem_Id(String name, UUID menuItemId);

    MenuVariant create(MenuVariant entity);

    Page<MenuVariant> search(MenuVariantSearchCriteria criteria, Pageable pageable);

    MenuVariant findById(UUID id);

    MenuVariant update(UUID id, MenuVariant entity);

    MenuVariant delete(UUID id);

    MenuVariant restore(UUID id);
}
