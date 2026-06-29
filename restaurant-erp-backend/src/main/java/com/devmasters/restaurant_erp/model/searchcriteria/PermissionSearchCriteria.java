package com.devmasters.restaurant_erp.model.searchcriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionSearchCriteria {

    private String code;

    private String name;

    private String module;

    private Boolean isActive;
}
