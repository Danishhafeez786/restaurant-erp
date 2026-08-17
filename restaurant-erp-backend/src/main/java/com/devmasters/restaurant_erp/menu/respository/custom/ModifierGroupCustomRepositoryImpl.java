package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.ModifierGroup;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.ModifierGroupSearchCriteria;
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

        if(criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
            String keyword = criteria.getSearchInput();
            filters.add(new Criteria().orOperator(
                            Criteria.where("name").regex(keyword, "i"),
                            Criteria.where("code").regex(keyword, "i")
                    )
            );

        }


        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getBranchId() != null)
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));


        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));


        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

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
