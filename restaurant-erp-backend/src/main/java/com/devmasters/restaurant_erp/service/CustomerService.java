package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Customer;
import com.devmasters.restaurant_erp.model.searchcriteria.CustomerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {

    boolean existsByCustomerCodeIgnoreCase(String customerCode);

    boolean existsByPhone(String phone);

    boolean existsByEmailIgnoreCase(String email);

    Customer create(Customer entity);

    Page<Customer> search(
            CustomerSearchCriteria criteria,
            Pageable pageable
    );

    Customer findById(UUID id);

    Customer update(UUID id, Customer entity);

    Customer delete(UUID id);

    Customer restore(UUID id);
}