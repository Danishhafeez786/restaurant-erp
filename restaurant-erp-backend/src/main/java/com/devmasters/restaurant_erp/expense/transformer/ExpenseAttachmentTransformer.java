package com.devmasters.restaurant_erp.expense.transformer;

import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.expense.domain.ExpenseAttachment;
import com.devmasters.restaurant_erp.expense.model.ExpenseAttachmentModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseAttachmentTransformer
        extends Transformer<ExpenseAttachment, ExpenseAttachmentModel> {

    @Override
    public ExpenseAttachment toEntity(
            ExpenseAttachmentModel model) {

        if (model == null) {
            return null;
        }

        return ExpenseAttachment.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .attachmentType(model.getAttachmentType())
                .fileName(model.getFileName())
                .originalFileName(model.getOriginalFileName())
                .fileUrl(model.getFileUrl())
                .contentType(model.getContentType())
                .fileSize(model.getFileSize())
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public ExpenseAttachmentModel toModel(
            ExpenseAttachment entity) {

        return ExpenseAttachmentModel.builder()
                .id(entity.getId())
                .expenseId(entity.getExpense() != null ? entity.getExpense().getId() : null)
                .attachmentType(entity.getAttachmentType())
                .fileName(entity.getFileName())
                .originalFileName(entity.getOriginalFileName())
                .fileUrl(entity.getFileUrl())
                .contentType(entity.getContentType())
                .fileSize(entity.getFileSize())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}