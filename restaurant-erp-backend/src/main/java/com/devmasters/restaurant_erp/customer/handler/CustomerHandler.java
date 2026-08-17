package com.devmasters.restaurant_erp.customer.handler;

import com.devmasters.restaurant_erp.customer.domain.Customer;
import com.devmasters.restaurant_erp.customer.model.CustomerModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.customer.model.searchCriteria.CustomerSearchCriteria;
import com.devmasters.restaurant_erp.customer.service.CustomerService;
import com.devmasters.restaurant_erp.customer.transformer.CustomerTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CustomerHandler {

    private final CustomerService customerService;
    private final CustomerTransformer customerTransformer;

    public CustomerModel create(CustomerModel model) {

        if (customerService.existsByCustomerCodeIgnoreCase(model.getCustomerCode())) {
            throw new RuntimeException(
                    "Customer already exists with code : " + model.getCustomerCode()
            );
        }

        if (customerService.existsByPhone(model.getPhone())) {
            throw new RuntimeException(
                    "Customer already exists with phone : " + model.getPhone()
            );
        }

        if (model.getEmail() != null
                && !model.getEmail().isBlank()
                && customerService.existsByEmailIgnoreCase(model.getEmail())) {

            throw new RuntimeException(
                    "Customer already exists with email : " + model.getEmail()
            );
        }

        Customer entity = customerTransformer.toEntity(model);

        Customer saved = customerService.create(entity);

        return customerTransformer.toModel(saved);
    }

    public PageResponse<CustomerModel> getAll(
            CustomerSearchCriteria criteria,
            Pageable pageable) {

        Page<Customer> page =
                customerService.search(criteria, pageable);

        return PageResponse.<CustomerModel>builder()
                .content(
                        customerTransformer.toModels(page.getContent())
                )
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public CustomerModel update(
            UUID id,
            CustomerModel model) {

        Customer entity =
                customerTransformer.toEntity(model);

        Customer updated =
                customerService.update(id, entity);

        return customerTransformer.toModel(updated);
    }

    public CustomerModel delete(UUID id) {

        Customer deleted =
                customerService.delete(id);

        return customerTransformer.toModel(deleted);
    }

    public CustomerModel restore(UUID id) {

        Customer restored =
                customerService.restore(id);

        return customerTransformer.toModel(restored);
    }
}