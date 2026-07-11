package com.devmasters.restaurant_erp.repository.custom;


import com.devmasters.restaurant_erp.domain.Menu.MenuVariant;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuVariantSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuVariantCustomRepository {

    Page<MenuVariant> search(
            MenuVariantSearchCriteria criteria,
            Pageable pageable
    );
}
