package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.PaymentMethod;
import com.devmasters.restaurant_erp.model.searchcriteria.PaymentMethodSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentMethodService {

    boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId);

    boolean existsByMethodNameIgnoreCaseAndOrganization_Id(String methodName, UUID organizationId);

    PaymentMethod create(PaymentMethod entity);

    Page<PaymentMethod> search(PaymentMethodSearchCriteria criteria, Pageable pageable);

    PaymentMethod findById(UUID id);

    PaymentMethod update(UUID id, PaymentMethod entity);

    PaymentMethod delete(UUID id);

    PaymentMethod restore(UUID id);
}