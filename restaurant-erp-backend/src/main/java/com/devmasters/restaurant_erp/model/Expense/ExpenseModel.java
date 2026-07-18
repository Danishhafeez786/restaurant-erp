package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.PaymentMethodModel;
import com.devmasters.restaurant_erp.model.VendorModel;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseModel {

    private UUID id;

    @Size(max = 50, message = "Expense number cannot exceed 50 characters")
    private String expenseNo;

    @NotBlank(message = "Expense title is required")
    @Size(min = 2, max = 150, message = "Expense title must be between 2 and 150 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Subtotal cannot be negative")
    private BigDecimal subTotal;

    @NotNull(message = "Tax amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tax amount cannot be negative")
    private BigDecimal taxAmount;

    @NotNull(message = "Discount amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount cannot be negative")
    private BigDecimal discountAmount;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount cannot be negative")
    private BigDecimal totalAmount;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

    private LocalDate dueDate;

    private LocalDate paidDate;

    @Size(max = 50, message = "Invoice number cannot exceed 50 characters")
    private String invoiceNo;

    @Size(max = 50, message = "Receipt number cannot exceed 50 characters")
    private String receiptNo;

    @Size(max = 50, message = "Reference number cannot exceed 50 characters")
    private String referenceNo;

    @Size(max = 1000, message = "Remarks cannot exceed 1000 characters")
    private String remarks;

    private List<String> tags;

    @NotNull(message = "Reimbursable status is required")
    private Boolean reimbursable;

    @Valid
    @NotNull(message = "Expense category is required")
    private ExpenseCategoryModel category;

    @Valid
    @NotNull(message = "Expense type is required")
    private ExpenseTypeModel expenseType;

    @Valid
    @NotNull(message = "Payment method is required")
    private PaymentMethodModel paymentMethod;

    @Valid
    @NotNull(message = "Expense status is required")
    private ExpenseStatusModel status;

    @Valid
    private VendorModel vendor;

    @Valid
    private EmployeeModel employee;

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