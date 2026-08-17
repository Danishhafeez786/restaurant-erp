package com.devmasters.restaurant_erp.permission.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionModel {

    private UUID id;

    @NotBlank(message = "Module is required")
    @Size(min = 2, max = 100, message = "Module must be between 2 and 100 characters")
    private String module;

    private String code;

    private String name;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
