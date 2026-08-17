package com.devmasters.restaurant_erp.organization.model.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSearchCriteria {
    private String searchInput;
    private Boolean isActive;
}
