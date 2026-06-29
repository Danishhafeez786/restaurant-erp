package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Permission;
import com.devmasters.restaurant_erp.model.searchcriteria.PermissionSearchCriteria;
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
public class PermissionCustomRepositoryImpl
        implements PermissionCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Permission> search(
            PermissionSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getCode() != null
                && !criteria.getCode().isBlank()) {

            filters.add(
                    Criteria.where("code")
                            .regex(criteria.getCode(), "i")
            );
        }

        if (criteria.getName() != null
                && !criteria.getName().isBlank()) {

            filters.add(
                    Criteria.where("name")
                            .regex(criteria.getName(), "i")
            );
        }

        if (criteria.getModule() != null
                && !criteria.getModule().isBlank()) {

            filters.add(
                    Criteria.where("module")
                            .regex(criteria.getModule(), "i")
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
                Permission.class
        );

        query.with(pageable);

        List<Permission> permissions =
                mongoTemplate.find(
                        query,
                        Permission.class
                );

        return new PageImpl<>(
                permissions,
                pageable,
                total
        );
    }
}