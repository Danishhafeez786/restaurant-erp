package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.enums.RecurringFrequency;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Frequency is required")
    private RecurringFrequency frequency;

    @NotNull(message = "Interval value is required")
    @Min(value = 1, message = "Interval value must be at least 1")
    private Integer intervalValue;

    @Min(value = 1, message = "Generate day must be at least 1")
    private Integer generateDay;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate lastGeneratedDate;

    private LocalDate nextGenerationDate;

    @NotNull(message = "Auto generate is required")
    private Boolean autoGenerate;

    @NotNull(message = "Active status is required")
    private Boolean active;

    @Valid
    @NotNull(message = "Expense category is required")
    private ExpenseCategoryModel category;

    @Valid
    @NotNull(message = "Expense type is required")
    private ExpenseTypeModel expenseType;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    @Valid
    @NotNull(message = "Branch is required")
    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}