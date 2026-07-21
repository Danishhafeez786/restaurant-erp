package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.model.searchcriteria.OrganizationSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationCustomRepository {

    Page<Organization> search(OrganizationSearchCriteria criteria, Pageable pageable);
}