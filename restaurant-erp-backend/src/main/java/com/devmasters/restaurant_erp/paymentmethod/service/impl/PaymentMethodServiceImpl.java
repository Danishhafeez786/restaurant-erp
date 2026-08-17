package com.devmasters.restaurant_erp.paymentmethod.service.impl;

import com.devmasters.restaurant_erp.paymentmethod.domain.PaymentMethod;
import com.devmasters.restaurant_erp.paymentmethod.model.searchCriteria.PaymentMethodSearchCriteria;
import com.devmasters.restaurant_erp.paymentmethod.respository.PaymentMethodRepository;
import com.devmasters.restaurant_erp.paymentmethod.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository repository;

    @Override
    public boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId) {
        return repository.existsByCodeIgnoreCaseAndOrganization_Id(code, organizationId);
    }

    @Override
    public boolean existsByMethodNameIgnoreCaseAndOrganization_Id(String methodName, UUID organizationId) {
        return repository.existsByMethodNameIgnoreCaseAndOrganization_Id(
                methodName,
                organizationId);
    }

    @Override
    public PaymentMethod create(PaymentMethod entity) {
        return repository.save(entity);
    }

    @Override
    public Page<PaymentMethod> search(PaymentMethodSearchCriteria criteria, Pageable pageable) {
        return repository.search(criteria, pageable);
    }

    @Override
    public PaymentMethod findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment Method not found."));
    }

    @Override
    public PaymentMethod update(UUID id, PaymentMethod entity) {

        PaymentMethod existing = findById(id);
        existing.setMethodName(entity.getMethodName());
        existing.setDescription(entity.getDescription());
        existing.setOnline(entity.getOnline());
        existing.setCashBased(entity.getCashBased());
        existing.setIsActive(entity.getIsActive());

        return repository.save(existing);
    }

    @Override
    public PaymentMethod delete(UUID id) {

        PaymentMethod entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }

    @Override
    public PaymentMethod restore(UUID id) {

        PaymentMethod entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}
