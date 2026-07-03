package com.devmasters.restaurant_erp.repository.custom;


import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.model.searchcriteria.OrganizationSearchCriteria;
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
    public Page<Organization> search(OrganizationSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getOrganizationName() != null
                && !criteria.getOrganizationName().isBlank()) {

            filters.add(
                    Criteria.where("organizationName")
                            .regex(criteria.getOrganizationName(), "i")
            );
        }

        if (criteria.getOwnerName() != null
                && !criteria.getOwnerName().isBlank()) {

            filters.add(
                    Criteria.where("ownerName")
                            .regex(criteria.getOwnerName(), "i")
            );
        }

        if (criteria.getCity() != null
                && !criteria.getCity().isBlank()) {

            filters.add(
                    Criteria.where("city")
                            .regex(criteria.getCity(), "i")
            );
        }

        if (criteria.getCountry() != null
                && !criteria.getCountry().isBlank()) {

            filters.add(
                    Criteria.where("country")
                            .regex(criteria.getCountry(), "i")
            );
        }

        if (criteria.getBillingCycle() != null) {
            filters.add(
                    Criteria.where("billingCycle")
                            .is(criteria.getBillingCycle())
            );
        }

        if (criteria.getSubscriptionPlanId() != null) {
            filters.add(
                    Criteria.where("subscriptionPlan.$id")
                            .is(criteria.getSubscriptionPlanId())
            );
        }

        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive())
            );
        }

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            filters.toArray(new Criteria[0])
                    )
            );
        }

        long total = mongoTemplate.count(
                query,
                Organization.class
        );

        query.with(pageable);

        List<Organization> organizations =
                mongoTemplate.find(
                        query,
                        Organization.class
                );

        return new PageImpl<>(
                organizations,
                pageable,
                total
        );
    }
}

