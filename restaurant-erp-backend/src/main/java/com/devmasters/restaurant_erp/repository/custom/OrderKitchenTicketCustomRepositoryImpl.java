package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderKitchenTicketSearchCriteria;
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
public class OrderKitchenTicketCustomRepositoryImpl implements OrderKitchenTicketCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderKitchenTicket> search(OrderKitchenTicketSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {

            if (StringUtils.hasText(criteria.getSearchInput())) {

                String searchInput = criteria.getSearchInput().trim();

                filters.add(new Criteria().orOperator(Criteria.where("ticketNumber").regex(searchInput, "i"),

                        Criteria.where("note").regex(searchInput, "i")));
            }

            if (criteria.getStatus() != null) {

                filters.add(Criteria.where("status").is(criteria.getStatus()));
            }

            if (StringUtils.hasText(criteria.getKitchenStation())) {

                filters.add(Criteria.where("kitchenStation").regex(criteria.getKitchenStation().trim(), "i"));
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

            if (criteria.getSentAtFrom() != null) {

                filters.add(Criteria.where("sentAt").gte(criteria.getSentAtFrom()));
            }

            if (criteria.getSentAtTo() != null) {

                filters.add(Criteria.where("sentAt").lte(criteria.getSentAtTo()));
            }
        }

        if (!filters.isEmpty()) {

            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderKitchenTicket.class);

        query.with(pageable);

        List<OrderKitchenTicket> tickets = mongoTemplate.find(query, OrderKitchenTicket.class);

        return new PageImpl<>(tickets, pageable, total);
    }
}