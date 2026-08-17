package com.devmasters.restaurant_erp.organization.respository.custom;

import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.organization.model.searchCriteria.OrganizationSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationCustomRepository {

    Page<Organization> search(OrganizationSearchCriteria criteria, Pageable pageable);
}