package com.devmasters.restaurant_erp.model.Menu;

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
public class ModifierModel {

    private UUID id;

    @NotBlank(message = "Modifier name is required")
    @Size(min = 2, max = 100, message = "Modifier name must be between 2 and 100 characters")
    private String name;

    private String code;

    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    private String sku;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Cost price is required")
    @PositiveOrZero(message = "Cost price cannot be negative")
    private BigDecimal costPrice;

    @PositiveOrZero(message = "Calories cannot be negative")
    private Integer calories;

    @Min(value = 1, message = "Display order must be at least 1")
    private Integer displayOrder;

    @NotNull(message = "Inventory tracked status is required")
    private Boolean inventoryTracked;

    @NotNull(message = "Availability status is required")
    private Boolean available;

    @Valid
    @NotNull(message = "Modifier group is required")
    private ModifierGroupModel modifierGroupModel;

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
