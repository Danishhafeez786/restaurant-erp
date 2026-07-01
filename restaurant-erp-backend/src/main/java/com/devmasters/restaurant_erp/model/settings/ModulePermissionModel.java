package com.devmasters.restaurant_erp.model.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModulePermissionModel {

    private String module;

    private List<PermissionMatrixModel> permissions;

}
