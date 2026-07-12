package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Vendor;
import com.devmasters.restaurant_erp.repository.custom.VendorCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VendorRepository extends MongoRepository<Vendor, UUID>, VendorCustomRepository {

    boolean existsByVendorCodeIgnoreCaseAndOrganization_Id(String vendorCode, UUID organizationId);

    boolean existsByVendorNameIgnoreCaseAndOrganization_Id(String vendorName, UUID organizationId);

    boolean existsByEmailIgnoreCaseAndOrganization_Id(String email, UUID organizationId);
}