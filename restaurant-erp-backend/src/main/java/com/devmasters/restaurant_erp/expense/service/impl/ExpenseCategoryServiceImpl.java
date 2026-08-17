package com.devmasters.restaurant_erp.expense.service.impl;

import com.devmasters.restaurant_erp.expense.domain.ExpenseCategory;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseCategorySearchCriteria;
import com.devmasters.restaurant_erp.expense.respository.ExpenseCategoryRepository;
import com.devmasters.restaurant_erp.expense.service.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository repository;

    @Override
    public boolean existsByCategoryCodeIgnoreCaseAndOrganization_Id(String categoryCode, UUID organizationId) {

        return repository.existsByCategoryCodeIgnoreCaseAndOrganization_Id(
                categoryCode,
                organizationId);
    }

    @Override
    public boolean existsByCategoryNameIgnoreCaseAndOrganization_Id(String categoryName, UUID organizationId) {
        return repository.existsByCategoryNameIgnoreCaseAndOrganization_Id(
                categoryName,
                organizationId);
    }

    @Override
    public ExpenseCategory create(ExpenseCategory entity) {
        return repository.save(entity);
    }

    @Override
    public Page<ExpenseCategory> search(ExpenseCategorySearchCriteria criteria, Pageable pageable) {
        return repository.search(criteria, pageable);
    }

    @Override
    public ExpenseCategory findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense Category not found."));
    }

    @Override
    public ExpenseCategory update(UUID id, ExpenseCategory entity) {

        ExpenseCategory existing = findById(id);
        existing.setCategoryName(entity.getCategoryName());
        existing.setDescription(entity.getDescription());
        existing.setColor(entity.getColor());
        existing.setIcon(entity.getIcon());
        existing.setSortOrder(entity.getSortOrder());
        existing.setSystemDefined(entity.getSystemDefined());
        existing.setOrganization(entity.getOrganization());
        existing.setIsActive(entity.getIsActive());

        return repository.save(existing);
    }

    @Override
    public ExpenseCategory delete(UUID id) {

        ExpenseCategory category = findById(id);
        category.setIsActive(false);
        return repository.save(category);
    }

    @Override
    public ExpenseCategory restore(UUID id) {

        ExpenseCategory category = findById(id);
        category.setIsActive(true);
        return repository.save(category);
    }
}
