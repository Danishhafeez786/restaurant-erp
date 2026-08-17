package com.devmasters.restaurant_erp.vendor.service;

import com.devmasters.restaurant_erp.vendor.domain.Vendor;
import com.devmasters.restaurant_erp.vendor.model.searchCriteria.VendorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VendorService {

    boolean existsByVendorCodeIgnoreCaseAndOrganization_Id(String vendorCode, UUID organizationId);

    boolean existsByVendorNameIgnoreCaseAndOrganization_Id(String vendorName, UUID organizationId);

    boolean existsByEmailIgnoreCaseAndOrganization_Id(String email, UUID organizationId);

    Vendor create(Vendor entity);

    Page<Vendor> search(VendorSearchCriteria criteria, Pageable pageable);

    Vendor findById(UUID id);

    Vendor update(UUID id, Vendor entity);

    Vendor delete(UUID id);

    Vendor restore(UUID id);
}