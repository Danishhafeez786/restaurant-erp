package com.devmasters.restaurant_erp.paymentmethod.respository.custom;

import com.devmasters.restaurant_erp.paymentmethod.domain.PaymentMethod;
import com.devmasters.restaurant_erp.paymentmethod.model.searchCriteria.PaymentMethodSearchCriteria;
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
public class PaymentMethodCustomRepositoryImpl implements PaymentMethodCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<PaymentMethod> search(PaymentMethodSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();
        if (criteria.getMethodName() != null && !criteria.getMethodName().isBlank()) {
            filters.add(
                    Criteria.where("methodName")
                            .regex(criteria.getMethodName(), "i"));
        }

        if (criteria.getCode() != null && !criteria.getCode().isBlank()) {
            filters.add(
                    Criteria.where("code")
                            .regex(criteria.getCode(), "i"));
        }

        if (criteria.getDescription() != null && !criteria.getDescription().isBlank()) {
            filters.add(
                    Criteria.where("description")
                            .regex(criteria.getDescription(), "i"));
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId()));
        }

        if (criteria.getOnline() != null) {
            filters.add(
                    Criteria.where("online")
                            .is(criteria.getOnline()));
        }

        if (criteria.getCashBased() != null) {
            filters.add(
                    Criteria.where("cashBased")
                            .is(criteria.getCashBased()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, PaymentMethod.class);

        query.with(pageable);
        List<PaymentMethod> data = mongoTemplate.find(query, PaymentMethod.class);

        return new PageImpl<>(data, pageable, total);
    }
}