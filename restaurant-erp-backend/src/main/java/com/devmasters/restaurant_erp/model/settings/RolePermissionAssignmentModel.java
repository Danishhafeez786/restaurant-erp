package com.devmasters.restaurant_erp.model.settings;

import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Role is required")
    private UUID roleId;

    @NotNull(message = "Permission is required")
    private UUID permissionId;

    @NotNull(message = "Assigned status is required")
    private Boolean assigned;

    private Boolean isActive;
}
