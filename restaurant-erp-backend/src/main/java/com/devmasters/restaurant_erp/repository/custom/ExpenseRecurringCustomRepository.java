package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseRecurring;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseRecurringSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseRecurringCustomRepository {

    Page<ExpenseRecurring> search(ExpenseRecurringSearchCriteria criteria, Pageable pageable);
}