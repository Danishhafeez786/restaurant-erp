package com.devmasters.restaurant_erp.rolepermission.model.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionMatrixResponse {

    private List<RoleMatrixModel> roles;

    private List<ModulePermissionModel> modules;

    private List<RolePermissionAssignmentModel> assignments;

}
