package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.Modifier;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.ModifierSearchCriteria;
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
public class ModifierCustomRepositoryImpl implements ModifierCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Modifier> search(ModifierSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if(criteria.getName() != null && !criteria.getName().isBlank()) {
            filters.add(
                    Criteria.where("name")
                            .regex(criteria.getName(), "i")
            );
        }

        if(criteria.getCode() != null && !criteria.getCode().isBlank()) {
            filters.add(
                    Criteria.where("code")
                            .regex(criteria.getCode(), "i")
            );
        }

        if(criteria.getSku() != null && !criteria.getSku().isBlank()) {
            filters.add(
                    Criteria.where("sku")
                            .regex(criteria.getSku(), "i")
            );
        }

        if(criteria.getModifierGroupId() != null) {
            filters.add(
                    Criteria.where("modifierGroup.$id")
                            .is(criteria.getModifierGroupId())
            );
        }

        if(criteria.getOrganizationId() != null) {
            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId())
            );
        }

        if(criteria.getBranchId() != null) {
            filters.add(
                    Criteria.where("branch.$id")
                            .is(criteria.getBranchId())
            );
        }

        if(criteria.getInventoryTracked() != null) {
            filters.add(
                    Criteria.where("inventoryTracked")
                            .is(criteria.getInventoryTracked())
            );
        }

        if(criteria.getAvailable() != null) {
            filters.add(
                    Criteria.where("available")
                            .is(criteria.getAvailable())
            );
        }

        if(criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive())
            );
        }

        if(!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            filters.toArray(new Criteria[0])
                    )
            );
        }

        long total = mongoTemplate.count(query, Modifier.class);

        query.with(pageable);

        List<Modifier> modifiers =
                mongoTemplate.find(query, Modifier.class);

        return new PageImpl<>(
                modifiers,
                pageable,
                total
        );
    }
}
