package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseAttachment;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseAttachmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpenseAttachmentService {

    ExpenseAttachment create(ExpenseAttachment entity);

    Page<ExpenseAttachment> search(ExpenseAttachmentSearchCriteria criteria, Pageable pageable);

    ExpenseAttachment findById(UUID id);

    ExpenseAttachment update(UUID id, ExpenseAttachment entity);

    ExpenseAttachment delete(UUID id);

    ExpenseAttachment restore(UUID id);
}