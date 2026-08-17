package com.devmasters.restaurant_erp.expense.respository.custom;


import com.devmasters.restaurant_erp.expense.domain.ExpenseCategory;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseCategorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseCategoryCustomRepository {

    Page<ExpenseCategory> search(
            ExpenseCategorySearchCriteria criteria,
            Pageable pageable);
}
