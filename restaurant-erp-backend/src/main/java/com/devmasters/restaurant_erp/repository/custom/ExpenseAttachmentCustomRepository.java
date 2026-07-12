package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseAttachment;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseAttachmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseAttachmentCustomRepository {

    Page<ExpenseAttachment> search(
            ExpenseAttachmentSearchCriteria criteria,
            Pageable pageable);
}