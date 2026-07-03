package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.Customer;
import com.devmasters.restaurant_erp.model.searchcriteria.CustomerSearchCriteria;
import com.devmasters.restaurant_erp.repository.CustomerRepository;
import com.devmasters.restaurant_erp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public boolean existsByCustomerCodeIgnoreCase(String customerCode) {
        return customerRepository.existsByCustomerCodeIgnoreCase(customerCode);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return customerRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public Customer create(Customer entity) {
        return customerRepository.save(entity);
    }

    @Override
    public Page<Customer> search(
            CustomerSearchCriteria criteria,
            Pageable pageable) {

        return customerRepository.search(criteria, pageable);
    }

    @Override
    public Customer findById(UUID id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found."));
    }

    @Override
    public Customer update(
            UUID id,
            Customer entity) {

        Customer existing = findById(id);

        existing.setCustomerCode(entity.getCustomerCode());
        existing.setFullName(entity.getFullName());
        existing.setPhone(entity.getPhone());
        existing.setEmail(entity.getEmail());
        existing.setAddress(entity.getAddress());
        existing.setLoyaltyPoints(entity.getLoyaltyPoints());
        existing.setCreditBalance(entity.getCreditBalance());
        existing.setBranch(entity.getBranch());
        existing.setDateOfBirth(entity.getDateOfBirth());
        existing.setGender(entity.getGender());
        existing.setTotalOrders(entity.getTotalOrders());
        existing.setTotalSpent(entity.getTotalSpent());
        existing.setLastOrderDate(entity.getLastOrderDate());
        existing.setMembershipLevel(entity.getMembershipLevel());
        existing.setIsActive(entity.getIsActive());

        return customerRepository.save(existing);
    }

    @Override
    public Customer delete(UUID id) {

        Customer customer = findById(id);

        if (!Boolean.TRUE.equals(customer.getIsActive())) {
            throw new RuntimeException("Customer already deleted.");
        }

        customer.setIsActive(false);

        return customerRepository.save(customer);
    }

    @Override
    public Customer restore(UUID id) {

        Customer customer = findById(id);

        customer.setIsActive(true);

        return customerRepository.save(customer);
    }
}