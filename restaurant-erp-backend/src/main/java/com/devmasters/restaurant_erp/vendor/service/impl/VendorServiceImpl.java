package com.devmasters.restaurant_erp.vendor.service.impl;

import com.devmasters.restaurant_erp.vendor.domain.Vendor;
import com.devmasters.restaurant_erp.vendor.model.searchCriteria.VendorSearchCriteria;
import com.devmasters.restaurant_erp.vendor.respository.VendorRepository;
import com.devmasters.restaurant_erp.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository repository;

    @Override
    public boolean existsByVendorCodeIgnoreCaseAndOrganization_Id(String vendorCode, UUID organizationId) {

        return repository.existsByVendorCodeIgnoreCaseAndOrganization_Id(vendorCode, organizationId);
    }

    @Override
    public boolean existsByVendorNameIgnoreCaseAndOrganization_Id(String vendorName, UUID organizationId) {
        return repository.existsByVendorNameIgnoreCaseAndOrganization_Id(
                vendorName,
                organizationId);
    }

    @Override
    public boolean existsByEmailIgnoreCaseAndOrganization_Id(String email, UUID organizationId) {

        return repository.existsByEmailIgnoreCaseAndOrganization_Id(
                email,
                organizationId);
    }

    @Override
    public Vendor create(Vendor entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Vendor> search(VendorSearchCriteria criteria, Pageable pageable) {

        return repository.search(criteria, pageable);
    }

    @Override
    public Vendor findById(UUID id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense Vendor not found."));
    }

    @Override
    public Vendor update(UUID id, Vendor entity) {

        Vendor existing = findById(id);

        existing.setVendorName(entity.getVendorName());
        existing.setContactPerson(entity.getContactPerson());
        existing.setPhone(entity.getPhone());
        existing.setAlternatePhone(entity.getAlternatePhone());
        existing.setEmail(entity.getEmail());
        existing.setWebsite(entity.getWebsite());
        existing.setTaxNumber(entity.getTaxNumber());
        existing.setRegistrationNumber(entity.getRegistrationNumber());
        existing.setAddress(entity.getAddress());
        existing.setCity(entity.getCity());
        existing.setState(entity.getState());
        existing.setCountry(entity.getCountry());
        existing.setZipCode(entity.getZipCode());
        existing.setNotes(entity.getNotes());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());

        return repository.save(existing);
    }

    @Override
    public Vendor delete(UUID id) {

        Vendor entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }

    @Override
    public Vendor restore(UUID id) {

        Vendor entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}
