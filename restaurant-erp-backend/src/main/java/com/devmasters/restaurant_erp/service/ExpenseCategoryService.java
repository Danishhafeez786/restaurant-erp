package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseCategory;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseCategorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpenseCategoryService {

    boolean existsByCategoryCodeIgnoreCaseAndOrganization_Id(String categoryCode, UUID organizationId);

    boolean existsByCategoryNameIgnoreCaseAndOrganization_Id(String categoryName, UUID organizationId);

    ExpenseCategory create(ExpenseCategory entity);

    Page<ExpenseCategory> search(ExpenseCategorySearchCriteria criteria, Pageable pageable);

    ExpenseCategory findById(UUID id);

    ExpenseCategory update(UUID id, ExpenseCategory entity);

    ExpenseCategory delete(UUID id);

    ExpenseCategory restore(UUID id);
}
