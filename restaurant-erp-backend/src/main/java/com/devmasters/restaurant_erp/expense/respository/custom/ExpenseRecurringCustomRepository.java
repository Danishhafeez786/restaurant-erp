package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseRecurring;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseRecurringSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseRecurringCustomRepository {

    Page<ExpenseRecurring> search(ExpenseRecurringSearchCriteria criteria, Pageable pageable);
}