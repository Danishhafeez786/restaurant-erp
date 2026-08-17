package com.devmasters.restaurant_erp.role.model.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleSearchCriteria {
    private String searchInput;
    private UUID organizationId;
    private Boolean isActive;
}