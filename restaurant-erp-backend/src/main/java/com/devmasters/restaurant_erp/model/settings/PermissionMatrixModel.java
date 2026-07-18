package com.devmasters.restaurant_erp.model.settings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionMatrixModel {

    private UUID id;

    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 100, message = "Permission name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Permission code is required")
    @Size(min = 2, max = 100, message = "Permission code must be between 2 and 100 characters")
    private String code;

    private Boolean isActive;
}
