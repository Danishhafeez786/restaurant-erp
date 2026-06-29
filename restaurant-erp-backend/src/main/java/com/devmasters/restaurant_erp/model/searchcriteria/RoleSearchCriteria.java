package com.devmasters.restaurant_erp.model.searchcriteria;

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

    private String roleName;

    private String description;

    private UUID organizationId;

    private Boolean isActive;
}