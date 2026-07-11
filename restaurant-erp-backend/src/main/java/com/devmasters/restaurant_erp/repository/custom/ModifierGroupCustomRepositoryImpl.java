package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Menu.ModifierGroup;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierGroupSearchCriteria;
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
public class ModifierGroupCustomRepositoryImpl implements ModifierGroupCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ModifierGroup> search(ModifierGroupSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getName() != null &&
                !criteria.getName().isBlank()) {
            filters.add(
                    Criteria.where("name")
                            .regex(criteria.getName(), "i")
            );
        }

        if (criteria.getCode() != null && !criteria.getCode().isBlank()) {
            filters.add(
                    Criteria.where("code")
                            .regex(criteria.getCode(), "i")
            );
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId())
            );
        }

        if (criteria.getBranchId() != null) {
            filters.add(Criteria.where("branch.$id")
                            .is(criteria.getBranchId())
            );
        }

        if (criteria.getRequired() != null) {
            filters.add(Criteria.where("required")
                            .is(criteria.getRequired())
            );
        }

        if (criteria.getIsActive() != null) {
            filters.add(Criteria.where("isActive")
                            .is(criteria.getIsActive())
            );
        }

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(
                            filters.toArray(new Criteria[0])
                    )
            );
        }

        long total = mongoTemplate.count(query, ModifierGroup.class);
        query.with(pageable);
        List<ModifierGroup> modifierGroups = mongoTemplate.find(
                        query,
                        ModifierGroup.class);
        return new PageImpl<>(
                modifierGroups,
                pageable,
                total
        );
    }
}
