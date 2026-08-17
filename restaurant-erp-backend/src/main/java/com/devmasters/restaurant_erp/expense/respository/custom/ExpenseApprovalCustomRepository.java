package com.devmasters.restaurant_erp.expense.respository.custom;

import com.devmasters.restaurant_erp.expense.domain.ExpenseApproval;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseApprovalSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseApprovalCustomRepository {

    Page<ExpenseApproval> search(
            ExpenseApprovalSearchCriteria criteria,
            Pageable pageable);
}