package com.devmasters.restaurant_erp.tax.service.impl;

import com.devmasters.restaurant_erp.tax.domain.Tax;
import com.devmasters.restaurant_erp.tax.model.searchCriteria.TaxSearchCriteria;
import com.devmasters.restaurant_erp.tax.respository.TaxRepository;
import com.devmasters.restaurant_erp.tax.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    @Override
    public boolean existsByTaxCodeIgnoreCase(String taxCode, UUID organizationId) {
        return taxRepository.existsByTaxCodeIgnoreCaseAndOrganization_IdAndIsActiveTrue(taxCode, organizationId);
    }

    @Override
    public boolean existsByTaxNameIgnoreCase(String taxName, UUID organizationId) {
        return taxRepository.existsByTaxNameIgnoreCaseAndOrganization_IdAndIsActiveTrue(taxName, organizationId);
    }

    @Override
    public boolean existsByTaxCodeIgnoreCaseAndIdNot(String taxCode, UUID organizationId, UUID id) {
        return taxRepository.existsByTaxCodeIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
                taxCode, organizationId, id);
    }

    @Override
    public boolean existsByTaxNameIgnoreCaseAndIdNot(String taxName, UUID organizationId, UUID id) {
        return taxRepository.existsByTaxNameIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
                taxName, organizationId, id);
    }

    @Override
    public boolean existsByDefaultTax(UUID organizationId) {
        return taxRepository.existsByDefaultTaxTrueAndOrganization_IdAndIsActiveTrue(organizationId);
    }

    @Override
    public boolean existsByDefaultTaxAndIdNot(UUID organizationId, UUID id) {
        return taxRepository.existsByDefaultTaxTrueAndOrganization_IdAndIsActiveTrueAndIdNot(
                organizationId, id);
    }

    @Override
    public Tax create(Tax entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);
        return taxRepository.save(entity);
    }

    @Override
    public Page<Tax> search(TaxSearchCriteria criteria, Pageable pageable) {
        return taxRepository.search(criteria, pageable);
    }

    @Override
    public Tax findById(UUID id) {
        return taxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found."));
    }

    @Override
    public Tax update(UUID id, Tax entity) {
        Tax existing = findById(id);

        existing.setTaxName(entity.getTaxName());
        existing.setCalculationType(entity.getCalculationType());
        existing.setRate(entity.getRate());
        existing.setDescription(entity.getDescription());
        existing.setDefaultTax(entity.getDefaultTax());
        existing.setBranch(entity.getBranch());
        existing.setUpdatedAt(LocalDateTime.now());

        return taxRepository.save(existing);
    }

    @Override
    public Tax delete(UUID id) {
        Tax tax = findById(id);
        tax.setIsActive(false);
        tax.setUpdatedAt(LocalDateTime.now());
        return taxRepository.save(tax);
    }

    @Override
    public Tax restore(UUID id) {
        Tax tax = findById(id);
        tax.setIsActive(true);
        tax.setUpdatedAt(LocalDateTime.now());
        return taxRepository.save(tax);
    }
}