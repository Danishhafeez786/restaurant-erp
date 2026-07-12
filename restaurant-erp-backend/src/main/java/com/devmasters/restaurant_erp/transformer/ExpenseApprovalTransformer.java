package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseApproval;
import com.devmasters.restaurant_erp.model.Expense.ExpenseApprovalModel;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExpenseApprovalTransformer extends Transformer<ExpenseApproval, ExpenseApprovalModel> {

    @Override
    public ExpenseApproval toEntity(ExpenseApprovalModel model) {

        if (model == null) {
            return null;
        }

        return ExpenseApproval.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .approvalLevel(model.getApprovalLevel())
                .approvalStatus(model.getApprovalStatus())
                .approved(model.getApproved())
                .remarks(model.getRemarks())
                .submittedAt(model.getSubmittedAt())
                .approvedAt(model.getApprovedAt())
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }


    @Override
    public ExpenseApprovalModel toModel(
            ExpenseApproval entity) {

        if (entity == null) {
            return null;
        }

        return ExpenseApprovalModel.builder()
                .id(entity.getId())
                .expenseId(entity.getExpense() != null ? entity.getExpense().getId() : null)
                .approvedById(entity.getApprovedBy() != null ? entity.getApprovedBy().getId() : null)
                .approvalLevel(entity.getApprovalLevel())
                .approvalStatus(entity.getApprovalStatus())
                .approved(entity.getApproved())
                .remarks(entity.getRemarks())
                .submittedAt(entity.getSubmittedAt())
                .approvedAt(entity.getApprovedAt())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }
}