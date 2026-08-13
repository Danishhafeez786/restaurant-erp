package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDeliverySearchCriteria;
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
public class OrderDeliveryCustomRepositoryImpl implements OrderDeliveryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderDelivery> search(OrderDeliverySearchCriteria criteria, Pageable pageable) {
        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getOrderId() != null)
            filters.add(Criteria.where("order.$id").is(criteria.getOrderId()));

        if (criteria.getStatus() != null)
            filters.add(Criteria.where("status").is(criteria.getStatus()));

        if (criteria.getDeliveryPartnerId() != null && !criteria.getDeliveryPartnerId().isBlank())
            filters.add(Criteria.where("deliveryPartnerId").is(criteria.getDeliveryPartnerId()));

        if (criteria.getOrganizationId() != null)
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));

        if (criteria.getBranchId() != null)
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));

        if (criteria.getAssignedById() != null)
            filters.add(Criteria.where("assignedBy.$id").is(criteria.getAssignedById()));

        if (criteria.getIsActive() != null)
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));

        if (!filters.isEmpty())
            query.addCriteria(new Criteria().andOperator(filters));

        long total = mongoTemplate.count(query, OrderDelivery.class);
        query.with(pageable);

        return new PageImpl<>(
                mongoTemplate.find(query, OrderDelivery.class),
                pageable,
                total
        );
    }
}