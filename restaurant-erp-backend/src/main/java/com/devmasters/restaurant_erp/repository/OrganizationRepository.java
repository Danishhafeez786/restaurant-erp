package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.repository.custom.OrganizationCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository
        extends MongoRepository<Organization, UUID>,
        OrganizationCustomRepository {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByOrganizationNameIgnoreCase(String organizationName);
}
