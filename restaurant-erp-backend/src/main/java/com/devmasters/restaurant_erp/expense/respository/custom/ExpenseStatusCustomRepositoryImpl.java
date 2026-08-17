package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseStatus;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseStatusSearchCriteria;
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
public class ExpenseStatusCustomRepositoryImpl implements ExpenseStatusCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ExpenseStatus> search(
            ExpenseStatusSearchCriteria criteria,
            Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getStatusName() != null &&
                !criteria.getStatusName().isBlank()) {
            filters.add(Criteria.where("statusName")
                    .regex(criteria.getStatusName(), "i"));
        }

        if (criteria.getCode() != null &&
                !criteria.getCode().isBlank()) {
            filters.add(Criteria.where("code")
                    .regex(criteria.getCode(), "i"));
        }

        if (criteria.getColor() != null &&
                !criteria.getColor().isBlank()) {
            filters.add(Criteria.where("color")
                    .regex(criteria.getColor(), "i"));
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(Criteria.where("organization.$id")
                    .is(criteria.getOrganizationId()));
        }

        if (criteria.getDefaultStatus() != null) {
            filters.add(Criteria.where("defaultStatus")
                    .is(criteria.getDefaultStatus()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(Criteria.where("isActive")
                    .is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, ExpenseStatus.class);
        query.with(pageable);
        List<ExpenseStatus> data = mongoTemplate.find(query, ExpenseStatus.class);
        return new PageImpl<>(data, pageable, total);
    }
}