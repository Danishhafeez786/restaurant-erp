package com.devmasters.restaurant_erp.model;

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

    private String methodName;

    private String code;

    private String description;

    private Boolean online;

    private Boolean cashBased;

    private OrganizationModel organizationModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}