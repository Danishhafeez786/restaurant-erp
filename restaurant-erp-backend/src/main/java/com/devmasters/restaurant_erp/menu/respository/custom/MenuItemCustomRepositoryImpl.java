package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.MenuItem;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemSearchCriteria;
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
public class MenuItemCustomRepositoryImpl
        implements MenuItemCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<MenuItem> search(
            MenuItemSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getName() != null &&
                !criteria.getName().isBlank()) {

            filters.add(
                    Criteria.where("name")
                            .regex(criteria.getName(), "i")
            );
        }

        if (criteria.getCode() != null &&
                !criteria.getCode().isBlank()) {

            filters.add(
                    Criteria.where("code")
                            .regex(criteria.getCode(), "i")
            );
        }

        if (criteria.getItemType() != null) {

            filters.add(
                    Criteria.where("itemType")
                            .is(criteria.getItemType())
            );
        }

        if (criteria.getCategoryId() != null) {

            filters.add(
                    Criteria.where("category.$id")
                            .is(criteria.getCategoryId())
            );
        }

        if (criteria.getOrganizationId() != null) {

            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId())
            );
        }

        if (criteria.getBranchId() != null) {

            filters.add(
                    Criteria.where("branch.$id")
                            .is(criteria.getBranchId())
            );
        }

        if (criteria.getFeatured() != null) {

            filters.add(
                    Criteria.where("featured")
                            .is(criteria.getFeatured())
            );
        }

        if (criteria.getPopular() != null) {

            filters.add(
                    Criteria.where("popular")
                            .is(criteria.getPopular())
            );
        }

        if (criteria.getAvailabilityStatus() != null) {

            filters.add(
                    Criteria.where("availabilityStatus")
                            .is(criteria.getAvailabilityStatus())
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

        long total = mongoTemplate.count(query, MenuItem.class);

        query.with(pageable);

        List<MenuItem> menuItems =
                mongoTemplate.find(query, MenuItem.class);

        return new PageImpl<>(
                menuItems,
                pageable,
                total
        );
    }
}