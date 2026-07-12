package com.devmasters.restaurant_erp.model.Expense;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseApprovalModel {

    private UUID id;

    private UUID expenseId;

    private UUID approvedById;

    private Integer approvalLevel;

    private String approvalStatus;

    private Boolean approved;

    private String remarks;

    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}