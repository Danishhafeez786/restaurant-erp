package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseType;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseTypeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseTypeCustomRepository {

    Page<ExpenseType> search(
            ExpenseTypeSearchCriteria criteria,
            Pageable pageable);
}