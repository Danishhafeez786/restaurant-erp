package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseStatus;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseStatusSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseStatusCustomRepository {

    Page<ExpenseStatus> search(ExpenseStatusSearchCriteria criteria, Pageable pageable);
}