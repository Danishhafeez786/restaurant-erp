package com.devmasters.restaurant_erp.expense.model;

import com.devmasters.restaurant_erp.common.enums.ApprovalStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseApprovalModel {

    private UUID id;

    @NotNull(message = "Expense is required")
    private UUID expenseId;

    @NotNull(message = "Approver is required")
    private UUID approvedById;

    @NotNull(message = "Approval level is required")
    @Min(value = 1, message = "Approval level must be at least 1")
    @Max(value = 10, message = "Approval level cannot exceed 10")
    private Integer approvalLevel;

    @NotBlank(message = "Approval status is required")
    private ApprovalStatus approvalStatus;

    @NotNull(message = "Approval decision is required")
    private Boolean approved;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}