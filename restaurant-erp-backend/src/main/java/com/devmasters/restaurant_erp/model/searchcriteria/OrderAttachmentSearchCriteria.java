package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.AttachmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAttachmentSearchCriteria {

    private UUID orderId;

    private AttachmentType attachmentType;

    private String fileType;

    private UUID organizationId;

    private UUID branchId;

    private Boolean isActive;
}