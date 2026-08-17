package com.devmasters.restaurant_erp.paymentmethod.model;

import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import jakarta.validation.Valid;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodModel {

    private UUID id;

    @NotBlank(message = "Payment method name is required")
    @Size(min = 2, max = 100, message = "Payment method name must be between 2 and 100 characters")
    private String methodName;

    @NotBlank(message = "Payment method code is required")
    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Online status is required")
    private Boolean online;

    @NotNull(message = "Cash based status is required")
    private Boolean cashBased;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}