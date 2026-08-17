package com.devmasters.restaurant_erp.role.respository.custom;

import com.devmasters.restaurant_erp.role.domain.Role;
import com.devmasters.restaurant_erp.role.model.searchCriteria.RoleSearchCriteria;
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
    public Page<Role> search(RoleSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {

            String keyword = criteria.getSearchInput();
            filters.add(new Criteria().orOperator(
                    Criteria.where("roleName").regex(keyword, "i"),
                    Criteria.where("description").regex(keyword, "i")
                    )
            );
        }

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, Role.class);

        query.with(pageable);

        List<Role> roles = mongoTemplate.find(query, Role.class);

        return new PageImpl<>(roles, pageable, total);
    }
}