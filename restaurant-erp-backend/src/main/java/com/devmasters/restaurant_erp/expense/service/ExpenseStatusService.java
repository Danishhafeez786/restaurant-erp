package com.devmasters.restaurant_erp.expense.service;

import com.devmasters.restaurant_erp.expense.domain.ExpenseStatus;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseStatusSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpenseStatusService {

    boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId);

    boolean existsByStatusNameIgnoreCaseAndOrganization_Id(String statusName, UUID organizationId);

    ExpenseStatus create(ExpenseStatus entity);

    Page<ExpenseStatus> search(ExpenseStatusSearchCriteria criteria, Pageable pageable);

    ExpenseStatus findById(UUID id);

    ExpenseStatus update(UUID id, ExpenseStatus entity);

    ExpenseStatus delete(UUID id);

    ExpenseStatus restore(UUID id);

    boolean existsByDefaultStatusTrueAndOrganization_Id(UUID organizationId);

    boolean existsByDefaultStatusTrueAndOrganization_IdAndIdNot(UUID organizationId,UUID id);
}