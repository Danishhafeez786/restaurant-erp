package com.devmasters.restaurant_erp.expense.service;

import com.devmasters.restaurant_erp.expense.domain.ExpenseAttachment;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseAttachmentSearchCriteria;
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