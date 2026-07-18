package com.devmasters.restaurant_erp.domain.Expense;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Employee;
import com.devmasters.restaurant_erp.enums.ApprovalStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("expense_approvals")
public class ExpenseApproval extends BaseEntity {

    @DBRef
    private Expense expense;

    @DBRef
    private Employee approvedBy;

    private Integer approvalLevel;

    private ApprovalStatus approvalStatus;
    // PENDING, APPROVED, REJECTED

    private Boolean approved;

    private String remarks;

    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;
}