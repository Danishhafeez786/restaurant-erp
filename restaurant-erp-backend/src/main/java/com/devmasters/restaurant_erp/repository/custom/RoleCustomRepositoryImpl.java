package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Role;
import com.devmasters.restaurant_erp.model.searchcriteria.RoleSearchCriteria;
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
public class RoleCustomRepositoryImpl
        implements RoleCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Role> search(
            RoleSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getRoleName() != null
                && !criteria.getRoleName().isBlank()) {

            filters.add(
                    Criteria.where("roleName")
                            .regex(criteria.getRoleName(), "i")
            );
        }

        if (criteria.getDescription() != null
                && !criteria.getDescription().isBlank()) {

            filters.add(
                    Criteria.where("description")
                            .regex(criteria.getDescription(), "i")
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

        long total = mongoTemplate.count(query, Role.class);

        query.with(pageable);

        List<Role> roles = mongoTemplate.find(query, Role.class);

        return new PageImpl<>(
                roles,
                pageable,
                total
        );
    }
}