package com.devmasters.restaurant_erp.customer.respository.custom;

import com.devmasters.restaurant_erp.customer.domain.Customer;
import com.devmasters.restaurant_erp.customer.model.searchCriteria.CustomerSearchCriteria;
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
public class CustomerCustomRepositoryImpl
        implements CustomerCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Customer> search(
            CustomerSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getCustomerCode() != null
                && !criteria.getCustomerCode().isBlank()) {

            filters.add(
                    Criteria.where("customerCode")
                            .regex(criteria.getCustomerCode(), "i")
            );
        }

        if (criteria.getFullName() != null
                && !criteria.getFullName().isBlank()) {

            filters.add(
                    Criteria.where("fullName")
                            .regex(criteria.getFullName(), "i")
            );
        }

        if (criteria.getPhone() != null
                && !criteria.getPhone().isBlank()) {

            filters.add(
                    Criteria.where("phone")
                            .regex(criteria.getPhone(), "i")
            );
        }

        if (criteria.getEmail() != null
                && !criteria.getEmail().isBlank()) {

            filters.add(
                    Criteria.where("email")
                            .regex(criteria.getEmail(), "i")
            );
        }

        if (criteria.getBranchId() != null) {

            filters.add(
                    Criteria.where("branch.$id")
                            .is(criteria.getBranchId())
            );
        }

        if (criteria.getMembershipLevel() != null) {

            filters.add(
                    Criteria.where("membershipLevel")
                            .is(criteria.getMembershipLevel())
            );
        }

        if (criteria.getGender() != null
                && !criteria.getGender().isBlank()) {

            filters.add(
                    Criteria.where("gender")
                            .regex(criteria.getGender(), "i")
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

        long total = mongoTemplate.count(query, Customer.class);

        query.with(pageable);

        List<Customer> customers =
                mongoTemplate.find(query, Customer.class);

        return new PageImpl<>(
                customers,
                pageable,
                total
        );
    }
}