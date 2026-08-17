package com.devmasters.restaurant_erp.tax.respository.custom;

import com.devmasters.restaurant_erp.tax.domain.Tax;
import com.devmasters.restaurant_erp.tax.model.searchCriteria.TaxSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxCustomRepository {

    Page<Tax> search(TaxSearchCriteria criteria, Pageable pageable);
}