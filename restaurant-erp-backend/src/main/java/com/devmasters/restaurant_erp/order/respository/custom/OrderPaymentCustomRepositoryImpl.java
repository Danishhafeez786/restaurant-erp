package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderPayment;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderPaymentSearchCriteria;
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
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class OrderPaymentCustomRepositoryImpl implements OrderPaymentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<OrderPayment> search(OrderPaymentSearchCriteria criteria, Pageable pageable) {
        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria != null) {
            if (criteria.getSearchInput() != null && !criteria.getSearchInput().isBlank()) {
                String regex = ".*" + Pattern.quote(criteria.getSearchInput().trim()) + ".*";

                filters.add(new Criteria().orOperator(
                        Criteria.where("paymentNumber").regex(regex, "i"),
                        Criteria.where("transactionReference").regex(regex, "i"),
                        Criteria.where("paymentNote").regex(regex, "i")
                ));
            }

            if (criteria.getStatus() != null) {
                filters.add(Criteria.where("status").is(criteria.getStatus()));
            }

            if (criteria.getOrderId() != null) {
                filters.add(Criteria.where("orderId").is(criteria.getOrderId()));
            }

            if (criteria.getOrganizationId() != null) {
                filters.add(Criteria.where("organization.$id").is(criteria.getOrganizationId()));
            }

            if (criteria.getBranchId() != null) {
                filters.add(Criteria.where("branch.$id").is(criteria.getBranchId()));
            }

            if (criteria.getPaymentMethodId() != null) {
                filters.add(Criteria.where("paymentMethod.$id").is(criteria.getPaymentMethodId()));
            }

            if (criteria.getReceivedById() != null) {
                filters.add(Criteria.where("receivedBy.$id").is(criteria.getReceivedById()));
            }
        }

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, OrderPayment.class);

        query.with(pageable);

        List<OrderPayment> payments = mongoTemplate.find(query, OrderPayment.class);

        return new PageImpl<>(payments, pageable, total);
    }
}