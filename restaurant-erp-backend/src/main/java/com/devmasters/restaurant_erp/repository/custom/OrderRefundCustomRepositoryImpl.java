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

        if (criteria.getKeyword() != null && !criteria.getKeyword().isBlank()) {
            String keyword = criteria.getKeyword().trim();

            filters.add(new Criteria().orOperator(
                    Criteria.where("refundNumber").regex(keyword, "i"),
                    Criteria.where("note").regex(keyword, "i"),
                    Criteria.where("transactionReference").regex(keyword, "i")
            ));
        }

        if (criteria.getOrderId() != null) {
            filters.add(Criteria.where("order.$id").is(criteria.getOrderId()));
        }

        if (criteria.getOrderPaymentId() != null) {
            filters.add(Criteria.where("orderPayment.$id").is(criteria.getOrderPaymentId()));
        }

        if (criteria.getStatus() != null) {
            filters.add(Criteria.where("status").is(criteria.getStatus()));
        }

        if (criteria.getReason() != null) {
            filters.add(Criteria.where("reason").is(criteria.getReason()));
        }

        if (criteria.getMinRefundAmount() != null ||
                criteria.getMaxRefundAmount() != null) {

            Criteria amountCriteria = Criteria.where("refundAmount");

            if (criteria.getMinRefundAmount() != null) {
                amountCriteria.gte(criteria.getMinRefundAmount());
            }

            if (criteria.getMaxRefundAmount() != null) {
                amountCriteria.lte(criteria.getMaxRefundAmount());
            }

            filters.add(amountCriteria);
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));
        }

        if (criteria.getBranchId() != null) {
            filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));
        }

        if (criteria.getProcessedById() != null) {
            filters.add(Criteria.where("processedBy.$id").is(criteria.getProcessedById()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(Criteria.where("isActive").is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters));
        }

        long total = mongoTemplate.count(query, OrderRefund.class);

        query.with(pageable);

        return new PageImpl<>(
                mongoTemplate.find(query, OrderRefund.class),
                pageable,
                total
        );
    }
}