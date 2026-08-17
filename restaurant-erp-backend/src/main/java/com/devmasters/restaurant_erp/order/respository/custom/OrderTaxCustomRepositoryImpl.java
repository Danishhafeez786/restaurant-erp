package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderTax;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderTaxSearchCriteria;
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
public class OrderTaxCustomRepositoryImpl implements OrderTaxCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderTax> search(OrderTaxSearchCriteria criteria, Pageable pageable) {
        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
            String search = criteria.getSearchInput().trim();

            filters.add(new Criteria().orOperator(
                    Criteria.where("taxNumber").regex(search, "i"),
                    Criteria.where("taxName").regex(search, "i")
            ));
        }

        if (criteria.getOrderId() != null)
            filters.add(Criteria.where("order.$id").is(criteria.getOrderId()));

        if (criteria.getTaxId() != null)
            filters.add(Criteria.where("tax.$id").is(criteria.getTaxId()));

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getBranchId() != null)
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));

        if (criteria.getAppliedById() != null)
            filters.add(Criteria.where("appliedBy.$id").is(criteria.getAppliedById()));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters));

        long total = mongoTemplate.count(query, OrderTax.class);
        query.with(pageable);

        return new PageImpl<>(
                mongoTemplate.find(query, OrderTax.class),
                pageable,
                total
        );
    }
}