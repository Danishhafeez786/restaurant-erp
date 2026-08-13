package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Tax;
import com.devmasters.restaurant_erp.model.searchcriteria.TaxSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxCustomRepository {

    Page<Tax> search(TaxSearchCriteria criteria, Pageable pageable);
}