package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Expense.Expense;
import com.devmasters.restaurant_erp.model.Expense.ExpenseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseTransformer extends Transformer<Expense, ExpenseModel> {

    private final ExpenseCategoryTransformer categoryTransformer;
    private final ExpenseTypeTransformer expenseTypeTransformer;
    private final PaymentMethodTransformer paymentMethodTransformer;
    private final ExpenseStatusTransformer statusTransformer;
    private final VendorTransformer vendorTransformer;
    private final EmployeeTransformer employeeTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public Expense toEntity(ExpenseModel model) {

        if (model == null) {
            return null;
        }

        return Expense.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .expenseNo(model.getExpenseNo())
                .title(model.getTitle())
                .description(model.getDescription())
                .subTotal(model.getSubTotal())
                .taxAmount(model.getTaxAmount())
                .discountAmount(model.getDiscountAmount())
                .totalAmount(model.getTotalAmount())
                .expenseDate(model.getExpenseDate())
                .dueDate(model.getDueDate())
                .paidDate(model.getPaidDate())
                .invoiceNo(model.getInvoiceNo())
                .receiptNo(model.getReceiptNo())
                .referenceNo(model.getReferenceNo())
                .remarks(model.getRemarks())
                .tags(model.getTags())
                .reimbursable(model.getReimbursable())
                .category(categoryTransformer.toEntity(model.getCategory()))
                .expenseType(expenseTypeTransformer.toEntity(model.getExpenseType()))
                .paymentMethod(paymentMethodTransformer.toEntity(model.getPaymentMethod()))
                .status(statusTransformer.toEntity(model.getStatus()))
                .vendor(vendorTransformer.toEntity(model.getVendor()))
                .employee(employeeTransformer.toEntity(model.getEmployee()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public ExpenseModel toModel(Expense entity) {

        if (entity == null) {
            return null;
        }

        return ExpenseModel.builder()
                .id(entity.getId())
                .expenseNo(entity.getExpenseNo())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .subTotal(entity.getSubTotal())
                .taxAmount(entity.getTaxAmount())
                .discountAmount(entity.getDiscountAmount())
                .totalAmount(entity.getTotalAmount())
                .expenseDate(entity.getExpenseDate())
                .dueDate(entity.getDueDate())
                .paidDate(entity.getPaidDate())
                .invoiceNo(entity.getInvoiceNo())
                .receiptNo(entity.getReceiptNo())
                .referenceNo(entity.getReferenceNo())
                .remarks(entity.getRemarks())
                .tags(entity.getTags())
                .reimbursable(entity.getReimbursable())
                .category(categoryTransformer.toModel(entity.getCategory()))
                .expenseType(expenseTypeTransformer.toModel(entity.getExpenseType()))
                .paymentMethod(paymentMethodTransformer.toModel(entity.getPaymentMethod()))
                .status(statusTransformer.toModel(entity.getStatus()))
                .vendor(vendorTransformer.toModel(entity.getVendor()))
                .employee(employeeTransformer.toModel(entity.getEmployee()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}