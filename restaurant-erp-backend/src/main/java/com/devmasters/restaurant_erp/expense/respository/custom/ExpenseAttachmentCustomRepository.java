package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseAttachment;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseAttachmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseAttachmentCustomRepository {

    Page<ExpenseAttachment> search(
            ExpenseAttachmentSearchCriteria criteria,
            Pageable pageable);
}