package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseStatus;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseStatusSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseStatusCustomRepository {

    Page<ExpenseStatus> search(ExpenseStatusSearchCriteria criteria, Pageable pageable);
}