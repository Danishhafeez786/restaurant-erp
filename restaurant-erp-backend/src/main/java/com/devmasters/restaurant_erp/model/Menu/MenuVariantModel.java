package com.devmasters.restaurant_erp.model.Menu;

import com.devmasters.restaurant_erp.enums.AvailabilityStatus;
import com.devmasters.restaurant_erp.enums.Unit;
import com.devmasters.restaurant_erp.enums.WeightUnit;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuVariantModel {

    private UUID id;

    @NotBlank(message = "Variant name is required")
    @Size(min = 2, max = 100, message = "Variant name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Variant code is required")
    @Size(min = 2, max = 30, message = "Variant code must be between 2 and 30 characters")
    private String code;

    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    private String sku;

    @Size(max = 50, message = "Barcode cannot exceed 50 characters")
    private String barcode;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Selling price cannot be negative")
    private BigDecimal sellingPrice;

    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cost price cannot be negative")
    private BigDecimal costPrice;

    @NotNull(message = "Preparation time is required")
    @PositiveOrZero(message = "Preparation time cannot be negative")
    private Integer preparationTime;

    @PositiveOrZero(message = "Calories cannot be negative")
    private Integer calories;

    @DecimalMin(value = "0.0", inclusive = true, message = "Weight cannot be negative")
    private Double weight;

    @NotNull(message = "Unit is required")
    private Unit unit;

    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder;

    @NotNull(message = "Default variant flag is required")
    private Boolean defaultVariant;

    @NotNull(message = "Inventory tracked flag is required")
    private Boolean inventoryTracked;

    @NotNull(message = "Availability status is required")
    private AvailabilityStatus availabilityStatus;

    @Valid
    @NotNull(message = "Menu item is required")
    private MenuItemModel menuItemModel;

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