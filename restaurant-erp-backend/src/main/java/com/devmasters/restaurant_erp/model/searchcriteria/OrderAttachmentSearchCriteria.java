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

    private String searchInput;

    private AttachmentType attachmentType;

    private UUID orderId;

    private UUID organizationId;

    private UUID branchId;
}