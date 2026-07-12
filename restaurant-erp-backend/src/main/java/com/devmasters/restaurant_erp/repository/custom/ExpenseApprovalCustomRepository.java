package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseApproval;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseApprovalSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseApprovalCustomRepository {

    Page<ExpenseApproval> search(
            ExpenseApprovalSearchCriteria criteria,
            Pageable pageable);
}