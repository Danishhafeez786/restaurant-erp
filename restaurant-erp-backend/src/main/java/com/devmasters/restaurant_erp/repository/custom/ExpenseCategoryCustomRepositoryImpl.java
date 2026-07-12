package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseCategory;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseCategorySearchCriteria;
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
public class ExpenseCategoryCustomRepositoryImpl implements ExpenseCategoryCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ExpenseCategory> search(ExpenseCategorySearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getCategoryName() != null && !criteria.getCategoryName().isBlank()) {
            filters.add(
                    Criteria.where("categoryName")
                            .regex(criteria.getCategoryName(), "i"));
        }

        if (criteria.getCategoryCode() != null && !criteria.getCategoryCode().isBlank()) {
            filters.add(
                    Criteria.where("categoryCode")
                            .regex(criteria.getCategoryCode(), "i"));
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

        if (criteria.getSystemDefined() != null) {
            filters.add(
                    Criteria.where("systemDefined")
                            .is(criteria.getSystemDefined()));
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

        long total = mongoTemplate.count(query, ExpenseCategory.class);
        query.with(pageable);
        List<ExpenseCategory> data = mongoTemplate.find(query, ExpenseCategory.class);

        return new PageImpl<>(data, pageable, total);
    }
}
