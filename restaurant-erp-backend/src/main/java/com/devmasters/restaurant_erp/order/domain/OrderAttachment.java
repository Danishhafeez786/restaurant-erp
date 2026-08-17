package com.devmasters.restaurant_erp.order.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.common.enums.AttachmentType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_attachments")
public class OrderAttachment extends BaseEntity {

    private String fileName;

    private String fileUrl;

    private String fileType;

    private Long fileSize;

    private AttachmentType attachmentType;

    private String description;

    @DBRef
    private Order order;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}