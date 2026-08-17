package com.devmasters.restaurant_erp.menu.model;

import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
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
public class ModifierGroupModel {

    private UUID id;

    @NotBlank(message = "Modifier group name is required")
    @Size(min = 2, max = 100, message = "Modifier group name must be between 2 and 100 characters")
    private String name;

    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

//    @NotNull(message = "Minimum selection is required")
    @Min(value = 0, message = "Minimum selection cannot be negative")
    @Max(value = 100, message = "Minimum selection cannot exceed 100")
    private Integer minimumSelection;

//    @NotNull(message = "Maximum selection is required")
    @Min(value = 1, message = "Maximum selection must be at least 1")
    @Max(value = 100, message = "Maximum selection cannot exceed 100")
    private Integer maximumSelection;

//    @NotNull(message = "Required flag is required")
    private Boolean required;

//    @NotNull(message = "Display order is required")
    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder;

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
