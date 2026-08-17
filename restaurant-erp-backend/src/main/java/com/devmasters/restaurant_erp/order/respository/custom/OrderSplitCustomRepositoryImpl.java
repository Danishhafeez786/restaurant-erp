package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderSplit;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSplitSearchCriteria;
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
public class OrderSplitCustomRepositoryImpl implements OrderSplitCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderSplit> search(OrderSplitSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getKeyword() != null && !criteria.getKeyword().isBlank()) {

            String keyword = criteria.getKeyword().trim();

            filters.add(new Criteria().orOperator(Criteria.where("splitNumber").regex(keyword, "i"), Criteria.where("note").regex(keyword, "i")));
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

        if (criteria.getPaid() != null) {
            filters.add(Criteria.where("paid").is(criteria.getPaid()));
        }

        if (criteria.getMinTotalAmount() != null || criteria.getMaxTotalAmount() != null) {

            Criteria amount = Criteria.where("totalAmount");

            if (criteria.getMinTotalAmount() != null) {
                amount.gte(criteria.getMinTotalAmount());
            }

            if (criteria.getMaxTotalAmount() != null) {
                amount.lte(criteria.getMaxTotalAmount());
            }

            filters.add(amount);
        }

        if (criteria.getIsActive() != null) {
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters));
        }

        long total = mongoTemplate.count(query, OrderSplit.class);

        query.with(pageable);

        return new PageImpl<>(mongoTemplate.find(query, OrderSplit.class), pageable, total);
    }
}