package com.devmasters.restaurant_erp.model.Menu;

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
public class ModifierModel {

    private UUID id;

    private String name;
    private String code;
    private String sku;

    private BigDecimal price;
    private BigDecimal costPrice;

    private Integer calories;
    private Integer displayOrder;

    private Boolean inventoryTracked;
    private Boolean available;

    private ModifierGroupModel modifierGroupModel;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
