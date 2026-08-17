package com.devmasters.restaurant_erp.expense.service.impl;

import com.devmasters.restaurant_erp.expense.domain.ExpenseRecurring;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseRecurringSearchCriteria;
import com.devmasters.restaurant_erp.expense.respository.ExpenseRecurringRepository;
import com.devmasters.restaurant_erp.expense.service.ExpenseRecurringService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ExpenseRecurringServiceImpl implements ExpenseRecurringService {

    private final ExpenseRecurringRepository repository;

    @Override
    public ExpenseRecurring create(ExpenseRecurring entity) {
        return repository.save(entity);
    }


    @Override
    public Page<ExpenseRecurring> search(ExpenseRecurringSearchCriteria criteria, Pageable pageable) {
        return repository.search(
                criteria,
                pageable);
    }


    @Override
    public ExpenseRecurring findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense Recurring not found."));
    }


    @Override
    public ExpenseRecurring update(UUID id, ExpenseRecurring entity) {

        ExpenseRecurring existing = findById(id);
        existing.setTitle(entity.getTitle());
        existing.setAmount(entity.getAmount());
        existing.setFrequency(entity.getFrequency());
        existing.setIntervalValue(entity.getIntervalValue());
        existing.setGenerateDay(entity.getGenerateDay());
        existing.setStartDate(entity.getStartDate());
        existing.setEndDate(entity.getEndDate());
        existing.setNextGenerationDate(entity.getNextGenerationDate());
        existing.setAutoGenerate(entity.getAutoGenerate());
        existing.setActive(entity.getActive());
        existing.setCategory(entity.getCategory());
        existing.setExpenseType(entity.getExpenseType());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());

        return repository.save(existing);
    }


    @Override
    public ExpenseRecurring delete(UUID id) {

        ExpenseRecurring entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }


    @Override
    public ExpenseRecurring restore(UUID id) {

        ExpenseRecurring entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}