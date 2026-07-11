package com.devmasters.restaurant_erp.model.Menu;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
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

    private String name;

    private String code;

    private String description;

    private Integer minimumSelection;

    private Integer maximumSelection;

    private Boolean required;

    private Integer displayOrder;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
