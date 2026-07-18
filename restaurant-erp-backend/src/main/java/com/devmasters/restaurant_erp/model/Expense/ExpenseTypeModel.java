package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.OrganizationModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Expense type name is required")
    @Size(min = 2, max = 100, message = "Expense type name must be between 2 and 100 characters")
    private String typeName;

    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Requires approval is required")
    private Boolean requiresApproval;

    @NotNull(message = "Requires attachment is required")
    private Boolean requiresAttachment;

    @NotNull(message = "Taxable status is required")
    private Boolean taxable;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
