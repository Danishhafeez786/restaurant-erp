package com.devmasters.restaurant_erp.menu.respository;

import com.devmasters.restaurant_erp.menu.domain.MenuVariant;
import com.devmasters.restaurant_erp.menu.respository.custom.MenuVariantCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuVariantRepository extends MongoRepository<MenuVariant, UUID>, MenuVariantCustomRepository {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);

    boolean existsBySkuIgnoreCaseAndBranch_Id(String sku, UUID branchId);

    boolean existsByBarcodeAndBranch_Id(String barcode, UUID branchId);

    boolean existsByNameIgnoreCaseAndMenuItem_Id(String name, UUID menuItemId);
}
