package com.devmasters.restaurant_erp.expense.handler;

import com.devmasters.restaurant_erp.expense.domain.Expense;
import com.devmasters.restaurant_erp.expense.domain.ExpenseAttachment;
import com.devmasters.restaurant_erp.expense.model.ExpenseAttachmentModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseAttachmentSearchCriteria;
import com.devmasters.restaurant_erp.expense.respository.ExpenseRepository;
import com.devmasters.restaurant_erp.expense.service.ExpenseAttachmentService;
import com.devmasters.restaurant_erp.expense.transformer.ExpenseAttachmentTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseAttachmentHandler {

    private final ExpenseAttachmentService service;
    private final ExpenseAttachmentTransformer transformer;
    private final ExpenseRepository expenseRepository;

    public ExpenseAttachmentModel create(ExpenseAttachmentModel model) {

        if (model.getExpenseId() == null) {
            throw new RuntimeException(
                    "Expense id is required.");
        }

        Expense expense =
                expenseRepository.findById(
                                model.getExpenseId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Expense not found."));

        validateFile(model);
        ExpenseAttachment attachment = transformer.toEntity(model);
        attachment.setExpense(expense);
        if (attachment.getIsActive() == null) {
            attachment.setIsActive(true);
        }
        return transformer.toModel(
                service.create(attachment));
    }

    public PageResponse<ExpenseAttachmentModel> getAll(ExpenseAttachmentSearchCriteria criteria, Pageable pageable) {
        Page<ExpenseAttachment> page =
                service.search(
                        criteria,
                        pageable);

        return PageResponse.<ExpenseAttachmentModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public ExpenseAttachmentModel update(UUID id, ExpenseAttachmentModel model) {
        return transformer.toModel(
                service.update(
                        id,
                        transformer.toEntity(model)));
    }

    public ExpenseAttachmentModel delete(UUID id) {
        return transformer.toModel(
                service.delete(id));
    }

    public ExpenseAttachmentModel restore(UUID id) {
        return transformer.toModel(
                service.restore(id));
    }

    private void validateFile(ExpenseAttachmentModel model) {

        if (model.getFileSize() != null &&
                model.getFileSize() > 10_000_000) {
            throw new RuntimeException(
                    "File size cannot exceed 10MB.");
        }

        if (model.getContentType() != null &&
                !(model.getContentType().equals("application/pdf")
                        || model.getContentType().startsWith("image/"))) {
            throw new RuntimeException(
                    "Only PDF and image files are allowed.");
        }
    }
}