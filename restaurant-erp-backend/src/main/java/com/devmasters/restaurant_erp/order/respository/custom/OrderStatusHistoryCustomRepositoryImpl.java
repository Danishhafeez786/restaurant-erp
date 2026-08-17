package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderStatusHistory;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderStatusHistorySearchCriteria;
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
public class OrderStatusHistoryCustomRepositoryImpl implements OrderStatusHistoryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderStatusHistory> search(OrderStatusHistorySearchCriteria criteria, Pageable pageable) {
        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getOrderId() != null)
            filters.add(Criteria.where("order.$id").is(criteria.getOrderId()));

        if (criteria.getPreviousStatus() != null)
            filters.add(Criteria.where("previousStatus").is(criteria.getPreviousStatus()));

        if (criteria.getNewStatus() != null)
            filters.add(Criteria.where("newStatus").is(criteria.getNewStatus()));

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getBranchId() != null)
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));

        if (criteria.getChangedById() != null)
            filters.add(Criteria.where("changedBy.$id").is(criteria.getChangedById()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters));

        long total = mongoTemplate.count(query, OrderStatusHistory.class);
        query.with(pageable);

        return new PageImpl<>(
                mongoTemplate.find(query, OrderStatusHistory.class),
                pageable,
                total
        );
    }
}