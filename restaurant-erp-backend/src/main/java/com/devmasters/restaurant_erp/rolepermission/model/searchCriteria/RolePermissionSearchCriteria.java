package com.devmasters.restaurant_erp.rolepermission.model.searchCriteria;

import lombok.Data;

import java.util.UUID;

@Data
public class RolePermissionSearchCriteria {

    private UUID organizationId;

    private UUID roleId;

    private UUID permissionId;

    private Boolean isActive;

}
