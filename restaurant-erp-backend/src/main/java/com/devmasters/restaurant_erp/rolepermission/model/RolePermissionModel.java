package com.devmasters.restaurant_erp.rolepermission.model;

import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.permission.model.PermissionModel;
import com.devmasters.restaurant_erp.role.model.RoleModel;
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
public class RolePermissionModel {

    private UUID id;
    private OrganizationModel organizationModel;
    private RoleModel roleModel;
    private PermissionModel permissionModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
