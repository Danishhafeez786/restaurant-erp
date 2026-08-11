package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderSplit;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderSplitSearchCriteria;
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
public class OrderSplitCustomRepositoryImpl implements OrderSplitCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderSplit> search(OrderSplitSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {

            if (StringUtils.hasText(criteria.getSearchInput())) {

                String searchInput = criteria.getSearchInput().trim();

                filters.add(new Criteria().orOperator(

                        Criteria.where("orderNumber").regex(searchInput, "i")));
            }

            if (criteria.getPaymentMethod() != null) {

                filters.add(Criteria.where("paymentMethod").is(criteria.getPaymentMethod()));
            }

            if (criteria.getPaid() != null) {

                filters.add(Criteria.where("paid").is(criteria.getPaid()));
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

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderSplit.class);

        query.with(pageable);

        List<OrderSplit> splits = mongoTemplate.find(query, OrderSplit.class);

        return new PageImpl<>(splits, pageable, total);
    }
}