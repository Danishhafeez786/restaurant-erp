package com.devmasters.restaurant_erp.customer.respository.custom;

import com.devmasters.restaurant_erp.customer.domain.Customer;
import com.devmasters.restaurant_erp.customer.model.searchCriteria.CustomerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerCustomRepository {

    Page<Customer> search(
            CustomerSearchCriteria criteria,
            Pageable pageable
    );
}