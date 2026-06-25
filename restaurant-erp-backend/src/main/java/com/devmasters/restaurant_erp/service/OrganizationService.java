package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.model.searchcriteria.OrganizationSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrganizationService {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByOrganizationNameIgnoreCase(String organizationName);

    Organization create(Organization entity);

    Page<Organization> search(OrganizationSearchCriteria criteria, Pageable pageable);

    Organization update(UUID id, Organization entity);

    Organization findById(UUID id);

    Organization restore(UUID id);

    Organization delete(UUID id);
}
