package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.Expense;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseSearchCriteria;
import com.devmasters.restaurant_erp.model.searchcriteria.BigDecimalRange;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ExpenseCustomRepositoryImpl implements ExpenseCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Expense> search(ExpenseSearchCriteria criteria,Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getExpenseNo() != null && !criteria.getExpenseNo().isBlank()) {
            filters.add(
                    Criteria.where("expenseNo")
                            .regex(criteria.getExpenseNo(), "i"));
        }

        if (criteria.getTitle() != null && !criteria.getTitle().isBlank()) {
            filters.add(
                    Criteria.where("title")
                            .regex(criteria.getTitle(), "i"));
        }

        if (criteria.getInvoiceNo() != null && !criteria.getInvoiceNo().isBlank()) {
            filters.add(
                    Criteria.where("invoiceNo")
                            .regex(criteria.getInvoiceNo(), "i"));
        }

        if (criteria.getReceiptNo() != null && !criteria.getReceiptNo().isBlank()) {
            filters.add(
                    Criteria.where("receiptNo")
                            .regex(criteria.getReceiptNo(), "i"));
        }

        if (criteria.getReferenceNo() != null && !criteria.getReferenceNo().isBlank()) {
            filters.add(
                    Criteria.where("referenceNo")
                            .regex(criteria.getReferenceNo(), "i"));
        }

        if (criteria.getCategoryId() != null) {
            filters.add(
                    Criteria.where("category.$id")
                            .is(criteria.getCategoryId()));
        }

        if (criteria.getExpenseTypeId() != null) {
            filters.add(
                    Criteria.where("expenseType.$id")
                            .is(criteria.getExpenseTypeId()));
        }

        if (criteria.getPaymentMethodId() != null) {
            filters.add(
                    Criteria.where("paymentMethod.$id")
                            .is(criteria.getPaymentMethodId()));
        }

        if (criteria.getStatusId() != null) {
            filters.add(
                    Criteria.where("status.$id")
                            .is(criteria.getStatusId()));
        }

        if (criteria.getVendorId() != null) {
            filters.add(
                    Criteria.where("vendor.$id")
                            .is(criteria.getVendorId()));
        }

        if (criteria.getEmployeeId() != null) {
            filters.add(
                    Criteria.where("employee.$id")
                            .is(criteria.getEmployeeId()));
        }

        if (criteria.getOrganizationId() != null) {
            filters.add(
                    Criteria.where("organization.$id")
                            .is(criteria.getOrganizationId()));
        }

        if (criteria.getBranchId() != null) {
            filters.add(
                    Criteria.where("branch.$id")
                            .is(criteria.getBranchId()));
        }

        if (criteria.getFromDate() != null) {
            filters.add(
                    Criteria.where("expenseDate")
                            .gte(criteria.getFromDate()));
        }

        if (criteria.getToDate() != null) {
            filters.add(
                    Criteria.where("expenseDate")
                            .lte(criteria.getToDate()));
        }

        if (criteria.getAmountRange() != null) {
            BigDecimalRange range = criteria.getAmountRange();

            if (range.getMin() != null) {
                filters.add(
                        Criteria.where("totalAmount")
                                .gte(range.getMin()));
            }

            if (range.getMax() != null) {
                filters.add(
                        Criteria.where("totalAmount")
                                .lte(range.getMax()));
            }
        }

        if (criteria.getReimbursable() != null) {
            filters.add(
                    Criteria.where("reimbursable")
                            .is(criteria.getReimbursable()));
        }

        if (criteria.getIsActive() != null) {
            filters.add(
                    Criteria.where("isActive")
                            .is(criteria.getIsActive()));
        }

        if (!filters.isEmpty()) {
            query.addCriteria(
                    new Criteria()
                            .andOperator(
                                    filters.toArray(
                                            new Criteria[0])));
        }
        long total =
                mongoTemplate.count(
                        query,
                        Expense.class);
        query.with(pageable);

        List<Expense> expenses = mongoTemplate.find(query, Expense.class);
        return new PageImpl<>(
                expenses,
                pageable,
                total);
    }
}