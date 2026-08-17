package com.devmasters.restaurant_erp.expense.domain;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.paymentmethod.domain.PaymentMethod;
import com.devmasters.restaurant_erp.vendor.domain.Vendor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("expenses")
public class Expense extends BaseEntity {

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

    @DBRef
    private ExpenseCategory category;

    @DBRef
    private ExpenseType expenseType;

    @DBRef
    private PaymentMethod paymentMethod;

    @DBRef
    private ExpenseStatus status;

    @DBRef
    private Vendor vendor;

    @DBRef
    private Employee employee;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}