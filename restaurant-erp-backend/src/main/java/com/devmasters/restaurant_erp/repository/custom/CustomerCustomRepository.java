package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Customer;
import com.devmasters.restaurant_erp.model.searchcriteria.CustomerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerCustomRepository {

    Page<Customer> search(
            CustomerSearchCriteria criteria,
            Pageable pageable
    );
}