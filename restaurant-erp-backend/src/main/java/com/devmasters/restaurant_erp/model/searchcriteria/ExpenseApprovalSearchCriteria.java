package com.devmasters.restaurant_erp.model.searchcriteria;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseApprovalSearchCriteria {

    private UUID expenseId;

    private UUID approvedById;

    private Integer approvalLevel;

    private String approvalStatus;

    private Boolean approved;

    private Boolean isActive;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;
}