package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseApproval;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseApprovalSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExpenseApprovalCustomRepositoryImpl implements ExpenseApprovalCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ExpenseApproval> search(ExpenseApprovalSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getExpenseId() != null) {
            filters.add(
                    Criteria.where("expense.$id")
                            .is(criteria.getExpenseId()));
        }

        if (criteria.getApprovedById() != null) {
            filters.add(
                    Criteria.where("approvedBy.$id")
                            .is(criteria.getApprovedById()));
        }

        if (criteria.getApprovalLevel() != null) {
            filters.add(
                    Criteria.where("approvalLevel")
                            .is(criteria.getApprovalLevel()));
        }

        if (criteria.getApprovalStatus() != null && !criteria.getApprovalStatus().isBlank()) {
            filters.add(
                    Criteria.where("approvalStatus")
                            .regex(
                                    criteria.getApprovalStatus(),
                                    "i"));
        }

        if (criteria.getApproved() != null) {
            filters.add(
                    Criteria.where("approved")
                            .is(criteria.getApproved()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive()));
        }

        if (criteria.getFromDate() != null) {
            filters.add(
                    Criteria.where("submittedAt")
                            .gte(criteria.getFromDate()));
        }

        if (criteria.getToDate() != null) {
            filters.add(
                    Criteria.where("submittedAt")
                            .lte(criteria.getToDate()));
        }


        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria()
                            .andOperator(
                                    filters.toArray(
                                            new Criteria[0])));
        }


        long total = mongoTemplate.count(query, ExpenseApproval.class);
        query.with(pageable);
        List<ExpenseApproval> data = mongoTemplate.find(query, ExpenseApproval.class);
        return new PageImpl<>(data, pageable, total);
    }
}