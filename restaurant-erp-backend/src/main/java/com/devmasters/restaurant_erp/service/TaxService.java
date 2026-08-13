package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Tax;
import com.devmasters.restaurant_erp.model.searchcriteria.TaxSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaxService {

    boolean existsByTaxCodeIgnoreCase(String taxCode, UUID organizationId);

    boolean existsByTaxNameIgnoreCase(String taxName, UUID organizationId);

    boolean existsByTaxCodeIgnoreCaseAndIdNot(String taxCode, UUID organizationId, UUID id);

    boolean existsByTaxNameIgnoreCaseAndIdNot(String taxName, UUID organizationId, UUID id);

    boolean existsByDefaultTax(UUID organizationId);

    boolean existsByDefaultTaxAndIdNot(UUID organizationId, UUID id);

    Tax create(Tax entity);

    Page<Tax> search(TaxSearchCriteria criteria, Pageable pageable);

    Tax findById(UUID id);

    Tax update(UUID id, Tax entity);

    Tax delete(UUID id);

    Tax restore(UUID id);
}