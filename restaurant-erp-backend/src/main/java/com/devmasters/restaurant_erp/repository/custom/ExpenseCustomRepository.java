package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.Expense;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseCustomRepository {

    Page<Expense> search(
            ExpenseSearchCriteria criteria,
            Pageable pageable);
}