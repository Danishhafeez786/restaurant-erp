package com.devmasters.restaurant_erp.order.model;

import com.devmasters.restaurant_erp.common.enums.AttachmentType;
import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAttachmentModel {

    private UUID id;

    private String fileName;

    private String fileUrl;

    private String fileType;

    private Long fileSize;

    private AttachmentType attachmentType;

    private String description;

    private OrderModel orderModel;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}