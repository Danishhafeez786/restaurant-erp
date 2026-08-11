package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDiscountSearchCriteria;
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
public class OrderDiscountCustomRepositoryImpl implements OrderDiscountCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderDiscount> search(OrderDiscountSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
            String search = criteria.getSearchInput().trim();

            filters.add(new Criteria().orOperator(
                    Criteria.where("discountNumber").regex(search, "i"),
                    Criteria.where("discountName").regex(search, "i"),
                    Criteria.where("reason").regex(search, "i")
            ));
        }

        if (criteria.getDiscountType() != null)
            filters.add(Criteria.where("discountType").is(criteria.getDiscountType()));

        if (criteria.getOrderId() != null)
            filters.add(Criteria.where("orderId").is(criteria.getOrderId()));

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

        long total = mongoTemplate.count(query, OrderDiscount.class);

        query.with(pageable);

        List<OrderDiscount> content =
                mongoTemplate.find(query, OrderDiscount.class);

        return new PageImpl<>(content, pageable, total);
    }
}