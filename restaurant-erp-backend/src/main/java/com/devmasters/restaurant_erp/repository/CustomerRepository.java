package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Customer;
import com.devmasters.restaurant_erp.repository.custom.CustomerCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository
        extends MongoRepository<Customer, UUID>,
        CustomerCustomRepository {

    boolean existsByCustomerCodeIgnoreCase(String customerCode);

    boolean existsByPhone(String phone);

    boolean existsByEmailIgnoreCase(String email);
}