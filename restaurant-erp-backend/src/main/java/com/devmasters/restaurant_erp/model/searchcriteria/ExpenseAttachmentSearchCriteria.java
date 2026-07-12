package com.devmasters.restaurant_erp.model.searchcriteria;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseAttachmentSearchCriteria {

    private UUID expenseId;

    private String attachmentType;

    private String fileName;

    private String contentType;

    private Boolean isActive;
}