package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.PaymentMethodModel;
import com.devmasters.restaurant_erp.model.VendorModel;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
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

    private String expenseNo;

    private String title;

    private String description;

    private BigDecimal subTotal;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private LocalDate expenseDate;

    private LocalDate dueDate;

    private LocalDate paidDate;

    private String invoiceNo;

    private String receiptNo;

    private String referenceNo;

    private String remarks;

    private List<String> tags;

    private Boolean reimbursable;

    private ExpenseCategoryModel category;

    private ExpenseTypeModel expenseType;

    private PaymentMethodModel paymentMethod;

    private ExpenseStatusModel status;

    private VendorModel vendor;

    private EmployeeModel employee;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}