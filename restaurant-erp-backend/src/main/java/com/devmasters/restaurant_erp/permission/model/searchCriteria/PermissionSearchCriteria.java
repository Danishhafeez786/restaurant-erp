package com.devmasters.restaurant_erp.permission.model.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionSearchCriteria {
    private String searchInput;
    private Boolean isActive;
}
