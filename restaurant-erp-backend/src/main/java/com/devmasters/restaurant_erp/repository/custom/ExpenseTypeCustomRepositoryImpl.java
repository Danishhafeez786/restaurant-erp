package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseType;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseTypeSearchCriteria;
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
public class ExpenseTypeCustomRepositoryImpl implements ExpenseTypeCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ExpenseType> search(ExpenseTypeSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();

        List<Criteria> filters = new ArrayList<>();
        if (criteria.getTypeName() != null && !criteria.getTypeName().isBlank()) {
            filters.add(
                    Criteria.where("typeName")
                            .regex(criteria.getTypeName(), "i"));
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

        if (criteria.getRequiresApproval() != null) {
            filters.add(
                    Criteria.where("requiresApproval")
                            .is(criteria.getRequiresApproval()));
        }

        if (criteria.getRequiresAttachment() != null) {
            filters.add(
                    Criteria.where("requiresAttachment")
                            .is(criteria.getRequiresAttachment()));
        }

        if (criteria.getTaxable() != null) {
            filters.add(
                    Criteria.where("taxable")
                            .is(criteria.getTaxable()));
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

        long total = mongoTemplate.count(query, ExpenseType.class);
        query.with(pageable);
        List<ExpenseType> data = mongoTemplate.find(query, ExpenseType.class);
        return new PageImpl<>(data, pageable, total);
    }
}