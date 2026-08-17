package com.devmasters.restaurant_erp.menu.respository.custom;


import com.devmasters.restaurant_erp.menu.domain.MenuVariant;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuVariantSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuVariantCustomRepository {

    Page<MenuVariant> search(
            MenuVariantSearchCriteria criteria,
            Pageable pageable
    );
}
