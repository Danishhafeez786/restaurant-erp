package com.devmasters.restaurant_erp.menu.model;

import com.devmasters.restaurant_erp.common.enums.AvailabilityStatus;
import com.devmasters.restaurant_erp.common.enums.MenuItemType;
import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
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
public class MenuItemModel {

    private UUID id;

    @NotBlank(message = "Menu item name is required")
    @Size(min = 2, max = 100, message = "Menu item name must be between 2 and 100 characters")
    private String name;

    private String code;

    @NotNull(message = "Menu item type is required")
    private MenuItemType itemType;

    @Size(max = 255, message = "Short description cannot exceed 255 characters")
    private String shortDescription;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;

    private UUID taxGroupId;

    private UUID kitchenStationId;

    private Boolean hasVariants;

    private Boolean hasModifiers;

    private Boolean hasAddons;

    private Boolean inventoryTracked;

    private Boolean featured;

    private Boolean popular;

    private Boolean dineIn;

    private Boolean takeaway;

    private Boolean delivery;

    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder;

    @NotNull(message = "Availability status is required")
    private AvailabilityStatus availabilityStatus;

    @Valid
    @NotNull(message = "Category is required")
    private CategoryModel categoryModel;

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
