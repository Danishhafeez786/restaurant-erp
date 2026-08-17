package com.devmasters.restaurant_erp.expense.model.searchCriteria;

import com.devmasters.restaurant_erp.common.BigDecimalRange;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSearchCriteria {

    private String expenseNo;

    private String title;

    private String invoiceNo;

    private String receiptNo;

    private String referenceNo;

    private UUID categoryId;

    private UUID expenseTypeId;

    private UUID paymentMethodId;

    private UUID statusId;

    private UUID vendorId;

    private UUID employeeId;

    private UUID organizationId;

    private UUID branchId;

    private LocalDate fromDate;

    private LocalDate toDate;

    private BigDecimalRange amountRange;

    private Boolean reimbursable;

    private Boolean isActive;
}