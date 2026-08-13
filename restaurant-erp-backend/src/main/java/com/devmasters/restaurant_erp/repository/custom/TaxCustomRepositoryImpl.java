package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Tax;
import com.devmasters.restaurant_erp.model.searchcriteria.TaxSearchCriteria;
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
public class TaxCustomRepositoryImpl implements TaxCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Tax> search(TaxSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
            String search = criteria.getSearchInput().trim();

            filters.add(new Criteria().orOperator(
                    Criteria.where("taxCode").regex(search, "i"),
                    Criteria.where("taxName").regex(search, "i"),
                    Criteria.where("description").regex(search, "i")
            ));
        }

        if (criteria.getCalculationType() != null)
            filters.add(Criteria.where("calculationType").is(criteria.getCalculationType()));

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getBranchId() != null)
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));

        if (criteria.getDefaultTax() != null)
            filters.add(Criteria.where("defaultTax").is(criteria.getDefaultTax()));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters));

        long total = mongoTemplate.count(query, Tax.class);
        query.with(pageable);

        return new PageImpl<>(
                mongoTemplate.find(query, Tax.class),
                pageable,
                total
        );
    }
}