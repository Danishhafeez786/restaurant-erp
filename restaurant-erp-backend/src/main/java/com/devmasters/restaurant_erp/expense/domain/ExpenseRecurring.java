package com.devmasters.restaurant_erp.expense.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.enums.RecurringFrequency;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("expense_recurring")
public class ExpenseRecurring extends BaseEntity {


    private String title;

    private BigDecimal amount;

    private RecurringFrequency frequency;

    private Integer intervalValue;

    private Integer generateDay;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate lastGeneratedDate;

    private LocalDate nextGenerationDate;

    private Boolean autoGenerate;

    private Boolean active;

    @DBRef
    private ExpenseCategory category;

    @DBRef
    private ExpenseType expenseType;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}