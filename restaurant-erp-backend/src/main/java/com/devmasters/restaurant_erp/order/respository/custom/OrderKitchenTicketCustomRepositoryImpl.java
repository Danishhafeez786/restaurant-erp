package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderKitchenTicket;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderKitchenTicketSearchCriteria;
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
public class OrderKitchenTicketCustomRepositoryImpl implements OrderKitchenTicketCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderKitchenTicket> search(
            OrderKitchenTicketSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria.getTicketNumber() != null &&
                !criteria.getTicketNumber().isBlank()) {

            filters.add(
                    Criteria.where("ticketNumber")
                            .is(criteria.getTicketNumber())
            );
        }

        if (criteria.getOrderId() != null) {
            filters.add(
                    Criteria.where("order.$id")
                            .is(criteria.getOrderId())
            );
        }

        if (criteria.getStatus() != null) {
            filters.add(
                    Criteria.where("status")
                            .is(criteria.getStatus())
            );
        }

        if (criteria.getPriority() != null) {
            filters.add(
                    Criteria.where("priority")
                            .is(criteria.getPriority())
            );
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId())
            );
        }

        if (criteria.getBranchId() != null) {
            filters.add(
                    Criteria.where("branch.$id")
                            .is(criteria.getBranchId())
            );
        }

        if (criteria.getAssignedToId() != null) {
            filters.add(
                    Criteria.where("assignedTo.$id")
                            .is(criteria.getAssignedToId())
            );
        }

        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive())
            );
        }

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(filters)
            );
        }

        long total = mongoTemplate.count(
                query,
                OrderKitchenTicket.class
        );

        query.with(pageable);

        return new PageImpl<>(
                mongoTemplate.find(
                        query,
                        OrderKitchenTicket.class
                ),
                pageable,
                total
        );
    }
}