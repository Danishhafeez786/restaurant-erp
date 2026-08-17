package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.Expense;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseCustomRepository {

    Page<Expense> search(
            ExpenseSearchCriteria criteria,
            Pageable pageable);
}