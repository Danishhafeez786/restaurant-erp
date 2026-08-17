package com.devmasters.restaurant_erp.rolepermission.model.settings;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Module name is required")
    @Size(max = 100, message = "Module name cannot exceed 100 characters")
    private String module;

    @Valid
    @NotEmpty(message = "At least one permission is required")
    private List<PermissionMatrixModel> permissions;

}
