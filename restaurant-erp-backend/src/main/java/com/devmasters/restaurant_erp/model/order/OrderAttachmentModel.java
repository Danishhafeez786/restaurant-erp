package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.AttachmentType;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAttachmentModel {

    private UUID id;

    private String fileName;

    private String fileUrl;

    private String contentType;

    private Long fileSize;

    private AttachmentType attachmentType;


    private OrderModel order;

    private OrganizationModel organization;

    private BranchModel branch;
}

