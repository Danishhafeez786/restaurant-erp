package com.devmasters.restaurant_erp.model.Expense;

import com.devmasters.restaurant_erp.model.OrganizationModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

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
public class ExpenseCategoryModel {

    private UUID id;

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String categoryName;

    private String categoryCode;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Color must be a valid hex color (e.g. #FF5733)"
    )
    private String color;

    @Size(max = 100, message = "Icon name cannot exceed 100 characters")
    private String icon;

    @NotNull(message = "Sort order is required")
    @Min(value = 0, message = "Sort order cannot be negative")
    private Integer sortOrder;

    @NotNull(message = "System defined status is required")
    private Boolean systemDefined;

    @NotNull(message = "Active status is required")
    private Boolean active;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
