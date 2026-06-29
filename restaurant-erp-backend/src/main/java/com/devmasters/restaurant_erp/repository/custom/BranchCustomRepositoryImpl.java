package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.model.searchcriteria.BranchSearchCriteria;
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
public class BranchCustomRepositoryImpl
        implements BranchCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Branch> search(
            BranchSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getBranchName() != null
                && !criteria.getBranchName().isBlank()) {

            filters.add(
                    Criteria.where("branchName")
                            .regex(criteria.getBranchName(), "i")
            );
        }

        if (criteria.getBranchCode() != null
                && !criteria.getBranchCode().isBlank()) {

            filters.add(
                    Criteria.where("branchCode")
                            .regex(criteria.getBranchCode(), "i")
            );
        }

        if (criteria.getCity() != null
                && !criteria.getCity().isBlank()) {

            filters.add(
                    Criteria.where("city")
                            .regex(criteria.getCity(), "i")
            );
        }

        if (criteria.getPhone() != null
                && !criteria.getPhone().isBlank()) {

            filters.add(
                    Criteria.where("phone")
                            .regex(criteria.getPhone(), "i")
            );
        }

        if (criteria.getOrganizationId() != null) {

            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId())
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
                Branch.class
        );

        query.with(pageable);

        List<Branch> branches =
                mongoTemplate.find(
                        query,
                        Branch.class
                );

        return new PageImpl<>(
                branches,
                pageable,
                total
        );
    }
}