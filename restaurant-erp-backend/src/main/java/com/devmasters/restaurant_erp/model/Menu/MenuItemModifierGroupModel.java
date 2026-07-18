package com.devmasters.restaurant_erp.model.Menu;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import jakarta.validation.Valid;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class MenuItemModifierGroupModel {

    private UUID id;

    @Valid
    @NotNull(message = "Menu item is required")
    private MenuItemModel menuItemModel;

    @Valid
    @NotNull(message = "Modifier group is required")
    private ModifierGroupModel modifierGroupModel;

    @NotNull(message = "Display order is required")
    @Min(value = 0, message = "Display order cannot be negative")
    @Max(value = 9999, message = "Display order cannot exceed 9999")
    private Integer displayOrder;

    @NotNull(message = "Required flag is required")
    private Boolean required;

    @NotNull(message = "Minimum selection is required")
    @Min(value = 0, message = "Minimum selection cannot be negative")
    private Integer minimumSelection;

    @NotNull(message = "Maximum selection is required")
    @Min(value = 0, message = "Maximum selection cannot be negative")
    private Integer maximumSelection;

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
