package com.devmasters.restaurant_erp.model.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionAssignmentModel {

    private UUID rolePermissionId;

    private UUID roleId;

    private UUID permissionId;

    private Boolean assigned;

    private Boolean isActive;

}
