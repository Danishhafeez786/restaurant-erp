package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.RolePermission;
import com.devmasters.restaurant_erp.model.searchcriteria.RolePermissionSearchCriteria;
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
public class RolePermissionSearchRepositoryImpl implements RolePermissionSearchRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<RolePermission> search(RolePermissionSearchCriteria criteria,
                                       Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id")
                    .is(criteria.getOrganizationId()));

        if (criteria.getRoleId() != null)
            filters.add(Criteria.where("role.$id")
                    .is(criteria.getRoleId()));

        if (criteria.getPermissionId() != null)
            filters.add(Criteria.where("permission.$id")
                    .is(criteria.getPermissionId()));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive")
                    .is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, RolePermission.class);

        query.with(pageable);

        List<RolePermission> result = mongoTemplate.find(query, RolePermission.class);

        return new PageImpl<>(result, pageable, total);
    }
}