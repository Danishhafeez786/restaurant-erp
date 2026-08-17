package com.devmasters.restaurant_erp.branch.model.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchSearchCriteria {

    private String searchInput;

    private Boolean isActive;

    private UUID organizationId;
}