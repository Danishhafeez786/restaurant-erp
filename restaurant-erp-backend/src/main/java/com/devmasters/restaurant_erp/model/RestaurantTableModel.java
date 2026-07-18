package com.devmasters.restaurant_erp.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTableModel {

    private UUID id;

    @NotBlank(message = "Table number is required")
    @Size(min = 1, max = 20, message = "Table number must be between 1 and 20 characters")
    private String tableNumber;

    @NotBlank(message = "Table name is required")
    @Size(min = 2, max = 100, message = "Table name must be between 2 and 100 characters")
    private String tableName;

    @NotNull(message = "Table capacity is required")
    @Min(value = 1, message = "Table capacity must be at least 1")
    private Integer capacity;

    @Size(max = 255, message = "QR token cannot exceed 255 characters")
    private String qrToken;

    @Valid
    @NotNull(message = "Branch is required")
    private BranchModel branchModel;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
