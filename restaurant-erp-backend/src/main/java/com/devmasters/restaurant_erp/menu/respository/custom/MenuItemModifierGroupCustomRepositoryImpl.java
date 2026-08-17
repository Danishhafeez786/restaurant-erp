package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemModifierGroupSearchCriteria;
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
public class MenuItemModifierGroupCustomRepositoryImpl implements MenuItemModifierGroupCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<MenuItemModifierGroup> search(MenuItemModifierGroupSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if(criteria.getMenuItemId() != null) {
            filters.add(
                    Criteria.where("menuItem.$id")
                            .is(criteria.getMenuItemId())
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

        if(criteria.getRequired() != null) {
            filters.add(
                    Criteria.where("required")
                            .is(criteria.getRequired())
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
                    new Criteria()
                            .andOperator(
                                    filters.toArray(
                                            new Criteria[0])
                            )
            );
        }


        long total = mongoTemplate.count(query, MenuItemModifierGroup.class);
        query.with(pageable);
        List<MenuItemModifierGroup> data = mongoTemplate.find(query, MenuItemModifierGroup.class);
        return new PageImpl<>(
                data,
                pageable,
                total);
    }
}
