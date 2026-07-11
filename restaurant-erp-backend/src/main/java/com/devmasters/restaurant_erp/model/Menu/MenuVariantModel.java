package com.devmasters.restaurant_erp.model.Menu;

import com.devmasters.restaurant_erp.enums.AvailabilityStatus;
import com.devmasters.restaurant_erp.enums.Unit;
import com.devmasters.restaurant_erp.enums.WeightUnit;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
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

    private String name;

    private String code;

    private String sku;

    private String barcode;

    private BigDecimal sellingPrice;

    private BigDecimal costPrice;

    private Integer preparationTime;

    private Integer calories;

    private Double weight;

    private Unit unit;

    private Integer displayOrder;

    private Boolean defaultVariant;

    private Boolean inventoryTracked;

    private AvailabilityStatus availabilityStatus;

    private MenuItemModel menuItemModel;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}