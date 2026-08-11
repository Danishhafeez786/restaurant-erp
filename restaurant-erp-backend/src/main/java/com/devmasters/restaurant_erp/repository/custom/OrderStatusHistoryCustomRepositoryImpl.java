package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderStatusHistory;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderStatusHistorySearchCriteria;
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
public class OrderStatusHistoryCustomRepositoryImpl implements OrderStatusHistoryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderStatusHistory> search(OrderStatusHistorySearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {

            if (StringUtils.hasText(criteria.getSearchInput())) {

                String searchInput = criteria.getSearchInput().trim();

                filters.add(Criteria.where("reason").regex(searchInput, "i"));
            }

            if (criteria.getPreviousStatus() != null) {

                filters.add(Criteria.where("previousStatus").is(criteria.getPreviousStatus()));
            }

            if (criteria.getNewStatus() != null) {

                filters.add(Criteria.where("newStatus").is(criteria.getNewStatus()));
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

            if (criteria.getChangedAtFrom() != null) {

                filters.add(Criteria.where("changedAt").gte(criteria.getChangedAtFrom()));
            }

            if (criteria.getChangedAtTo() != null) {

                filters.add(Criteria.where("changedAt").lte(criteria.getChangedAtTo()));
            }
        }

        if (!filters.isEmpty()) {

            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        query.with(pageable.getSort());

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderStatusHistory.class);

        query.with(pageable);

        List<OrderStatusHistory> histories = mongoTemplate.find(query, OrderStatusHistory.class);

        return new PageImpl<>(histories, pageable, total);
    }
}