package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.OrganizationModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class ExpenseStatusModel {

    private UUID id;

    @NotBlank(message = "Status name is required")
    @Size(min = 2, max = 100, message = "Status name must be between 2 and 100 characters")
    private String statusName;

    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Color is required")
    @Size(max = 20, message = "Color cannot exceed 20 characters")
    private String color;

    @NotNull(message = "Display order is required")
    @Min(value = 1, message = "Display order must be at least 1")
    private Integer displayOrder;

    @NotNull(message = "Default status is required")
    private Boolean defaultStatus;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}