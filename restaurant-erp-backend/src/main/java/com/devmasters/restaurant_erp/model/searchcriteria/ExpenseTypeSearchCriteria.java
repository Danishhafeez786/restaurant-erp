package com.devmasters.restaurant_erp.model.searchcriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTypeSearchCriteria {

    private String typeName;

    private String code;

    private String description;

    private UUID organizationId;

    private Boolean requiresApproval;

    private Boolean requiresAttachment;

    private Boolean taxable;

    private Boolean isActive;
}
