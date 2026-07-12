package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseType;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseTypeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpenseTypeService {

    boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId);

    boolean existsByTypeNameIgnoreCaseAndOrganization_Id(String typeName, UUID organizationId);

    ExpenseType create(ExpenseType entity);

    Page<ExpenseType> search(ExpenseTypeSearchCriteria criteria, Pageable pageable);

    ExpenseType findById(UUID id);

    ExpenseType update(UUID id, ExpenseType entity);

    ExpenseType delete(UUID id);

    ExpenseType restore(UUID id);
}