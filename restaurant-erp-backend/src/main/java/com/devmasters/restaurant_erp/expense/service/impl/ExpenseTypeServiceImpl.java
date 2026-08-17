package com.devmasters.restaurant_erp.expense.service.impl;


import com.devmasters.restaurant_erp.expense.domain.ExpenseType;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseTypeSearchCriteria;
import com.devmasters.restaurant_erp.expense.respository.ExpenseTypeRepository;
import com.devmasters.restaurant_erp.expense.service.ExpenseTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseTypeServiceImpl implements ExpenseTypeService {

    private final ExpenseTypeRepository repository;

    @Override
    public boolean existsByCodeIgnoreCaseAndOrganization_Id(String code, UUID organizationId) {

        return repository.existsByCodeIgnoreCaseAndOrganization_Id(code, organizationId);
    }

    @Override
    public boolean existsByTypeNameIgnoreCaseAndOrganization_Id(String typeName, UUID organizationId) {
        return repository.existsByTypeNameIgnoreCaseAndOrganization_Id(
                typeName,
                organizationId);
    }

    @Override
    public ExpenseType create(ExpenseType entity) {
        return repository.save(entity);
    }

    @Override
    public Page<ExpenseType> search(ExpenseTypeSearchCriteria criteria, Pageable pageable) {
        return repository.search(
                criteria,
                pageable);
    }

    @Override
    public ExpenseType findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense Type not found."));
    }

    @Override
    public ExpenseType update(UUID id, ExpenseType entity) {

        ExpenseType existing = findById(id);
        existing.setTypeName(entity.getTypeName());
        existing.setDescription(entity.getDescription());
        existing.setRequiresApproval(entity.getRequiresApproval());
        existing.setRequiresAttachment(entity.getRequiresAttachment());
        existing.setTaxable(entity.getTaxable());
        existing.setIsActive(entity.getIsActive());

        return repository.save(existing);
    }

    @Override
    public ExpenseType delete(UUID id) {

        ExpenseType entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }

    @Override
    public ExpenseType restore(UUID id) {

        ExpenseType entity = findById(id);

        entity.setIsActive(true);
        return repository.save(entity);
    }
}
