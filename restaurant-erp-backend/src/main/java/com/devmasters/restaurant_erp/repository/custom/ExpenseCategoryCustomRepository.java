package com.devmasters.restaurant_erp.repository.custom;


import com.devmasters.restaurant_erp.domain.Expense.ExpenseCategory;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseCategorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseCategoryCustomRepository {

    Page<ExpenseCategory> search(
            ExpenseCategorySearchCriteria criteria,
            Pageable pageable);
}
