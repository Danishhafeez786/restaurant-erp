package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.MenuVariant;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuVariantSearchCriteria;
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
public class MenuVariantCustomRepositoryImpl
        implements MenuVariantCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<MenuVariant> search(
            MenuVariantSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            filters.add(Criteria.where("name")
                            .regex(criteria.getName(), "i"));
        }

        if (criteria.getCode() != null && !criteria.getCode().isBlank()) {
            filters.add(Criteria.where("code")
                            .regex(criteria.getCode(), "i"));
        }

        if (criteria.getSku() != null && !criteria.getSku().isBlank()) {
            filters.add(Criteria.where("sku")
                            .regex(criteria.getSku(), "i"));
        }

        if (criteria.getBarcode() != null && !criteria.getBarcode().isBlank()) {
            filters.add(Criteria.where("barcode")
                            .regex(criteria.getBarcode(), "i"));
        }

        if (criteria.getMenuItemId() != null) {
            filters.add(Criteria.where("menuItem.$id")
                            .is(criteria.getMenuItemId()));
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId()));
        }

        if (criteria.getBranchId() != null) {
            filters.add(Criteria.where("branch.$id")
                            .is(criteria.getBranchId()));
        }

        if (criteria.getDefaultVariant() != null) {
            filters.add(Criteria.where("defaultVariant")
                            .is(criteria.getDefaultVariant()));
        }

        if (criteria.getInventoryTracked() != null) {
            filters.add(Criteria.where("inventoryTracked")
                            .is(criteria.getInventoryTracked()));
        }

        if (criteria.getAvailabilityStatus() != null) {
            filters.add(Criteria.where("availabilityStatus")
                            .is(criteria.getAvailabilityStatus()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(Criteria.where("isActive")
                            .is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, MenuVariant.class);

        query.with(pageable);
        List<MenuVariant> variants = mongoTemplate.find(query, MenuVariant.class);

        return new PageImpl<>(
                variants,
                pageable,
                total
        );
    }
}