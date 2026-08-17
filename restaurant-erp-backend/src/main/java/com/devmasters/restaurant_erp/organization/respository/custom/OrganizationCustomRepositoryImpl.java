package com.devmasters.restaurant_erp.organization.respository.custom;


import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.organization.model.searchCriteria.OrganizationSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrganizationCustomRepositoryImpl
        implements OrganizationCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Organization> search(OrganizationSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
            filters.add(new Criteria().orOperator(
                    Criteria.where("organizationName").regex(criteria.getSearchInput(), "i"),
                    Criteria.where("ownerName").regex(criteria.getSearchInput(), "i"),
                    Criteria.where("city").regex(criteria.getSearchInput(), "i"),
                    Criteria.where("country").regex(criteria.getSearchInput(), "i")
            ));
        }

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, Organization.class);

        query.with(pageable);

        List<Organization> organizations = mongoTemplate.find(query, Organization.class);

        return new PageImpl<>(organizations, pageable, total);
    }
}

