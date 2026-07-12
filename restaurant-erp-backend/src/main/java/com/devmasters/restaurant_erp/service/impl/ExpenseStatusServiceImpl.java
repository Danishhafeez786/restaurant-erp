package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseStatus;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseStatusSearchCriteria;
import com.devmasters.restaurant_erp.repository.ExpenseStatusRepository;
import com.devmasters.restaurant_erp.service.ExpenseStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseStatusServiceImpl implements ExpenseStatusService {

    private final ExpenseStatusRepository repository;

    @Override
    public boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId) {
        return repository.existsByCodeIgnoreCaseAndOrganization_Id(
                code,
                organizationId);
    }

    @Override
    public boolean existsByStatusNameIgnoreCaseAndOrganization_Id(String statusName, UUID organizationId) {
        return repository.existsByStatusNameIgnoreCaseAndOrganization_Id(
                statusName,
                organizationId);
    }

    @Override
    public ExpenseStatus create(ExpenseStatus entity) {
        return repository.save(entity);
    }

    @Override
    public Page<ExpenseStatus> search(ExpenseStatusSearchCriteria criteria, Pageable pageable) {

        return repository.search(criteria, pageable);
    }

    @Override
    public ExpenseStatus findById(UUID id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense Status not found."));
    }

    @Override
    public ExpenseStatus update(UUID id, ExpenseStatus entity) {

        ExpenseStatus existing = findById(id);
        existing.setStatusName(entity.getStatusName());
        existing.setDescription(entity.getDescription());
        existing.setColor(entity.getColor());
        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setDefaultStatus(entity.getDefaultStatus());
        existing.setIsActive(entity.getIsActive());

        return repository.save(existing);
    }

    @Override
    public ExpenseStatus delete(UUID id) {

        ExpenseStatus entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }

    @Override
    public ExpenseStatus restore(UUID id) {

        ExpenseStatus entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }

    @Override
    public boolean existsByDefaultStatusTrueAndOrganization_Id(
            UUID organizationId) {

        return repository.existsByDefaultStatusTrueAndOrganization_Id(
                organizationId);
    }

    @Override
    public boolean existsByDefaultStatusTrueAndOrganization_IdAndIdNot(UUID organizationId, UUID id) {

        return repository.existsByDefaultStatusTrueAndOrganization_Id(
                organizationId);
    }
}
