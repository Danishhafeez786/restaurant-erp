package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTypeModel {

    private UUID id;

    private String typeName;

    private String code;

    private String description;

    private Boolean requiresApproval;

    private Boolean requiresAttachment;

    private Boolean taxable;

    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
