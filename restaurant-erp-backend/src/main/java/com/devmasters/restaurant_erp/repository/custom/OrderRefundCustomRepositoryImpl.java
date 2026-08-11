package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderRefund;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderRefundSearchCriteria;
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
public class OrderRefundCustomRepositoryImpl implements OrderRefundCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderRefund> search(OrderRefundSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {

            if (StringUtils.hasText(criteria.getSearchInput())) {

                String searchInput = criteria.getSearchInput().trim();

                filters.add(new Criteria().orOperator(Criteria.where("refundNumber").regex(searchInput, "i"),

                        Criteria.where("reason").regex(searchInput, "i")));
            }

            if (criteria.getPaymentMethod() != null) {

                filters.add(Criteria.where("paymentMethod").is(criteria.getPaymentMethod()));
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

            if (criteria.getRefundedAtFrom() != null) {

                filters.add(Criteria.where("refundedAt").gte(criteria.getRefundedAtFrom()));
            }

            if (criteria.getRefundedAtTo() != null) {

                filters.add(Criteria.where("refundedAt").lte(criteria.getRefundedAtTo()));
            }
        }

        if (!filters.isEmpty()) {

            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderRefund.class);

        query.with(pageable);

        List<OrderRefund> refunds = mongoTemplate.find(query, OrderRefund.class);

        return new PageImpl<>(refunds, pageable, total);
    }
}