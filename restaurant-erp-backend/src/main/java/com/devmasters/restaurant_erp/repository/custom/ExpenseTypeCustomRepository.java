package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseType;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseTypeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseTypeCustomRepository {

    Page<ExpenseType> search(
            ExpenseTypeSearchCriteria criteria,
            Pageable pageable);
}