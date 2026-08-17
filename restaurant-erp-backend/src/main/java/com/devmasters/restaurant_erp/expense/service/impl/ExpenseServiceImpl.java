package com.devmasters.restaurant_erp.expense.service.impl;

import com.devmasters.restaurant_erp.expense.domain.Expense;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseSearchCriteria;
import com.devmasters.restaurant_erp.expense.respository.ExpenseRepository;
import com.devmasters.restaurant_erp.expense.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;

    @Override
    public boolean existsByExpenseNoAndOrganization_Id(String expenseNo, UUID organizationId) {
        return repository.existsByExpenseNoAndOrganization_Id(
                expenseNo,
                organizationId);
    }

    @Override
    public Expense create(Expense entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Expense> search(ExpenseSearchCriteria criteria, Pageable pageable) {
        return repository.search(criteria, pageable);
    }

    @Override
    public Expense findById(
            UUID id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense not found."));
    }

    @Override
    public Expense update(UUID id, Expense entity) {

        Expense existing = findById(id);
        existing.setTitle(entity.getTitle());
        existing.setDescription(entity.getDescription());
        existing.setSubTotal(entity.getSubTotal());
        existing.setTaxAmount(entity.getTaxAmount());
        existing.setDiscountAmount(entity.getDiscountAmount());
        existing.setTotalAmount(entity.getTotalAmount());
        existing.setExpenseDate(entity.getExpenseDate());
        existing.setDueDate(entity.getDueDate());
        existing.setPaidDate(entity.getPaidDate());
        existing.setInvoiceNo(entity.getInvoiceNo());
        existing.setReceiptNo(entity.getReceiptNo());
        existing.setReferenceNo(entity.getReferenceNo());
        existing.setRemarks(entity.getRemarks());
        existing.setTags(entity.getTags());
        existing.setReimbursable(entity.getReimbursable());
        existing.setCategory(entity.getCategory());
        existing.setExpenseType(entity.getExpenseType());
        existing.setPaymentMethod(entity.getPaymentMethod());
        existing.setStatus(entity.getStatus());
        existing.setVendor(entity.getVendor());
        existing.setEmployee(entity.getEmployee());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());
        return repository.save(existing);
    }

    @Override
    public Expense delete(UUID id) {

        Expense entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }

    @Override
    public Expense restore(UUID id) {

        Expense entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}