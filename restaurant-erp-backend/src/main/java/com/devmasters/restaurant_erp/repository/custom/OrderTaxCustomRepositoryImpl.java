package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderTax;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderTaxSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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

        if (criteria != null) {

            if (StringUtils.hasText(criteria.getSearchInput())) {

                String searchInput = criteria.getSearchInput().trim();

                filters.add(new Criteria().orOperator(Criteria.where("taxCode").regex(searchInput, "i"),

                        Criteria.where("taxName").regex(searchInput, "i")));
            }

            if (criteria.getTaxType() != null) {

                filters.add(Criteria.where("taxType").is(criteria.getTaxType()));
            }

            if (criteria.getOrderId() != null) {

                filters.add(Criteria.where("order.$id").is(criteria.getOrderId()));
            }

            if (criteria.getOrganizationId() != null) {

                filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));
            }

            if (criteria.getBranchId() != null) {

                filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));
            }
        }

        if (!filters.isEmpty()) {

            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderTax.class);

        query.with(pageable);

        List<OrderTax> taxes = mongoTemplate.find(query, OrderTax.class);

        return new PageImpl<>(taxes, pageable, total);
    }
}