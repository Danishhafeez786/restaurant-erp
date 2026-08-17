package com.devmasters.restaurant_erp.organization.respository;

import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.organization.respository.custom.OrganizationCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, UUID>,
        OrganizationCustomRepository {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByOrganizationNameIgnoreCase(String organizationName);
}
