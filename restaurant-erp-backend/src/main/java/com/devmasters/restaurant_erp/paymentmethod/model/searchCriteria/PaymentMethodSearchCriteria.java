package com.devmasters.restaurant_erp.paymentmethod.model.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodSearchCriteria {

    private String methodName;

    private String code;

    private String description;

    private UUID organizationId;

    private Boolean online;

    private Boolean cashBased;

    private Boolean isActive;
}