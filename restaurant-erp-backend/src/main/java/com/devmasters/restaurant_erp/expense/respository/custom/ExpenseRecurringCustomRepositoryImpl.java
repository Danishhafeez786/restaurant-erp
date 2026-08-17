package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseRecurring;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseRecurringSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExpenseRecurringCustomRepositoryImpl implements ExpenseRecurringCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ExpenseRecurring> search(ExpenseRecurringSearchCriteria criteria, Pageable pageable) {


        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getTitle() != null && !criteria.getTitle().isBlank()) {
            filters.add(
                    Criteria.where("title")
                            .regex(criteria.getTitle(), "i"));
        }


        if (criteria.getFrequency() != null && !criteria.getFrequency().isBlank()) {
            filters.add(
                    Criteria.where("frequency").is(criteria.getFrequency()));
        }


        if (criteria.getAutoGenerate() != null) {
            filters.add(
                    Criteria.where("autoGenerate").is(criteria.getAutoGenerate()));
        }


        if (criteria.getActive() != null) {
            filters.add(
                    Criteria.where("active").is(criteria.getActive()));
        }


        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive").is(criteria.getIsActive()));
        }


        if (criteria.getCategoryId() != null) {
            filters.add(
                    Criteria.where("category.$id").is(criteria.getCategoryId()));
        }


        if (criteria.getExpenseTypeId() != null) {
            filters.add(
                    Criteria.where("expenseType.$id").is(criteria.getExpenseTypeId()));
        }


        if (criteria.getOrganizationId() != null) {
            filters.add(
                    Criteria.where("organization.$id").is(criteria.getOrganizationId()));
        }


        if (criteria.getBranchId() != null) {
            filters.add(
                    Criteria.where("branch.$id").is(criteria.getBranchId()));
        }


        if (criteria.getMinAmount() != null) {
            filters.add(
                    Criteria.where("amount").gte(criteria.getMinAmount()));
        }


        if (criteria.getMaxAmount() != null) {
            filters.add(
                    Criteria.where("amount").lte(criteria.getMaxAmount()));
        }


        if (criteria.getNextGenerationDateFrom() != null) {
            filters.add(
                    Criteria.where("nextGenerationDate")
                            .gte(criteria.getNextGenerationDateFrom()));
        }


        if (criteria.getNextGenerationDateTo() != null) {
            filters.add(
                    Criteria.where("nextGenerationDate")
                            .lte(criteria.getNextGenerationDateTo()));
        }


        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, ExpenseRecurring.class);
        query.with(pageable);
        List<ExpenseRecurring> data = mongoTemplate.find(query, ExpenseRecurring.class);
        return new PageImpl<>(data, pageable, total);
    }
}