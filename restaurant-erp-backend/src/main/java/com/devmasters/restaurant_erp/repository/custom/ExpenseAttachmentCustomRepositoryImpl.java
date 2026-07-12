package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseAttachment;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseAttachmentSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExpenseAttachmentCustomRepositoryImpl
        implements ExpenseAttachmentCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ExpenseAttachment> search(ExpenseAttachmentSearchCriteria criteria, Pageable pageable) {

        Query query = new Query();
        List<Criteria> filters = new ArrayList<>();
        if (criteria.getExpenseId() != null) {
            filters.add(
                    Criteria.where("expense.$id")
                            .is(criteria.getExpenseId()));
        }

        if (criteria.getAttachmentType() != null && !criteria.getAttachmentType().isBlank()) {
            filters.add(
                    Criteria.where("attachmentType")
                            .regex(criteria.getAttachmentType(), "i"));
        }

        if (criteria.getFileName() != null && !criteria.getFileName().isBlank()) {
            filters.add(
                    Criteria.where("fileName")
                            .regex(criteria.getFileName(), "i"));
        }

        if (criteria.getContentType() != null && !criteria.getContentType().isBlank()) {
            filters.add(
                    Criteria.where("contentType")
                            .is(criteria.getContentType()));
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

        long total = mongoTemplate.count(query, ExpenseAttachment.class);
        query.with(pageable);
        List<ExpenseAttachment> data = mongoTemplate.find(query, ExpenseAttachment.class);

        return new PageImpl<>(
                data,
                pageable,
                total);
    }
}