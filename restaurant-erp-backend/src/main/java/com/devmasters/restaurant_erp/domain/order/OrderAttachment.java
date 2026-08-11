package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.enums.AttachmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

    private String contentType;

    private Long fileSize;

    private AttachmentType attachmentType;


    @DBRef
    private Order order;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}