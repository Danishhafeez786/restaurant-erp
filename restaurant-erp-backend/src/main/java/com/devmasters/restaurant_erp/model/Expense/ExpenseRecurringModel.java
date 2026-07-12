package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRecurringModel {

    private UUID id;

    private String title;

    private BigDecimal amount;

    private String frequency;
    // DAILY
    // WEEKLY
    // MONTHLY
    // YEARLY

    private Integer intervalValue;

    private Integer generateDay;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate lastGeneratedDate;

    private LocalDate nextGenerationDate;

    private Boolean autoGenerate;

    private Boolean active;


    private ExpenseCategoryModel category;

    private ExpenseTypeModel expenseType;


    private OrganizationModel organizationModel;

    private BranchModel branchModel;


    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}