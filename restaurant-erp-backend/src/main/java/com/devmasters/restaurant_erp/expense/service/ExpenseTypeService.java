package com.devmasters.restaurant_erp.expense.service;

import com.devmasters.restaurant_erp.expense.domain.ExpenseType;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseTypeSearchCriteria;
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