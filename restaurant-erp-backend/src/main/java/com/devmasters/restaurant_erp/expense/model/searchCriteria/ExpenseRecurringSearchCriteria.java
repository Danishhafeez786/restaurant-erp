package com.devmasters.restaurant_erp.expense.model.searchCriteria;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRecurringSearchCriteria {

    private String title;

    private String frequency;

    private Boolean autoGenerate;

    private Boolean active;

    private Boolean isActive;


    private UUID categoryId;

    private UUID expenseTypeId;


    private UUID organizationId;

    private UUID branchId;


    private BigDecimal minAmount;

    private BigDecimal maxAmount;


    private LocalDate startDateFrom;

    private LocalDate startDateTo;


    private LocalDate nextGenerationDateFrom;

    private LocalDate nextGenerationDateTo;
}