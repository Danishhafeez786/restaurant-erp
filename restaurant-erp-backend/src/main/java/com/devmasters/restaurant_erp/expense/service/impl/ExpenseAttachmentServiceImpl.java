package com.devmasters.restaurant_erp.expense.service.impl;

import com.devmasters.restaurant_erp.expense.domain.ExpenseAttachment;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseAttachmentSearchCriteria;
import com.devmasters.restaurant_erp.expense.respository.ExpenseAttachmentRepository;
import com.devmasters.restaurant_erp.expense.service.ExpenseAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseAttachmentServiceImpl implements ExpenseAttachmentService {

    private final ExpenseAttachmentRepository repository;

    @Override
    public ExpenseAttachment create(ExpenseAttachment entity) {
        return repository.save(entity);
    }

    @Override
    public Page<ExpenseAttachment> search(ExpenseAttachmentSearchCriteria criteria, Pageable pageable) {
        return repository.search(
                criteria,
                pageable);
    }

    @Override
    public ExpenseAttachment findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Expense Attachment not found."));
    }

    @Override
    public ExpenseAttachment update(UUID id, ExpenseAttachment entity) {

        ExpenseAttachment existing = findById(id);
        existing.setAttachmentType(entity.getAttachmentType());
        existing.setFileName(entity.getFileName());
        existing.setOriginalFileName(entity.getOriginalFileName());
        existing.setFileUrl(entity.getFileUrl());
        existing.setContentType(entity.getContentType());
        existing.setFileSize(entity.getFileSize());
        existing.setIsActive(entity.getIsActive());
        return repository.save(existing);
    }

    @Override
    public ExpenseAttachment delete(UUID id) {

        ExpenseAttachment entity = findById(id);
        entity.setIsActive(false);
        return repository.save(entity);
    }

    @Override
    public ExpenseAttachment restore(UUID id) {

        ExpenseAttachment entity = findById(id);
        entity.setIsActive(true);
        return repository.save(entity);
    }
}