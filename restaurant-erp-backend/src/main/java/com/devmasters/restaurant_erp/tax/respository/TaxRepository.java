package com.devmasters.restaurant_erp.tax.respository;

import com.devmasters.restaurant_erp.tax.domain.Tax;
import com.devmasters.restaurant_erp.tax.respository.custom.TaxCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaxRepository extends MongoRepository<Tax, UUID>, TaxCustomRepository {

    boolean existsByTaxCodeIgnoreCaseAndOrganization_IdAndIsActiveTrue(String taxCode, UUID organizationId);

    boolean existsByTaxNameIgnoreCaseAndOrganization_IdAndIsActiveTrue(String taxName, UUID organizationId);

    boolean existsByTaxCodeIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
            String taxCode, UUID organizationId, UUID id);

    boolean existsByTaxNameIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
            String taxName, UUID organizationId, UUID id);

    boolean existsByDefaultTaxTrueAndOrganization_IdAndIsActiveTrue(UUID organizationId);

    boolean existsByDefaultTaxTrueAndOrganization_IdAndIsActiveTrueAndIdNot(
            UUID organizationId, UUID id);
}