package com.devmasters.restaurant_erp.expense.service.impl;

import com.devmasters.restaurant_erp.expense.domain.ExpenseApproval;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseApprovalSearchCriteria;
import com.devmasters.restaurant_erp.expense.respository.ExpenseApprovalRepository;
import com.devmasters.restaurant_erp.expense.service.ExpenseApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseApprovalServiceImpl implements ExpenseApprovalService {

    private final ExpenseApprovalRepository repository;


    @Override
    public ExpenseApproval create(ExpenseApproval entity) {
        return repository.save(entity);
    }


    @Override
    public Page<ExpenseApproval> search(ExpenseApprovalSearchCriteria criteria, Pageable pageable) {
        return repository.search(
                criteria,
                pageable);
    }


    @Override
    public ExpenseApproval findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense Approval not found."));
    }


    @Override
    public ExpenseApproval update(UUID id, ExpenseApproval entity) {

        ExpenseApproval existing = findById(id);
        existing.setApprovalLevel(entity.getApprovalLevel());
        existing.setApprovalStatus(entity.getApprovalStatus());
        existing.setApproved(entity.getApproved());
        existing.setRemarks(entity.getRemarks());
        existing.setApprovedAt(entity.getApprovedAt());
        existing.setIsActive(entity.getIsActive());

        return repository.save(existing);
    }


    @Override
    public ExpenseApproval delete(UUID id) {

        ExpenseApproval entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }


    @Override
    public ExpenseApproval restore(UUID id) {

        ExpenseApproval entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}