package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderAttachment;
import com.devmasters.restaurant_erp.model.order.OrderAttachmentModel;
import org.springframework.stereotype.Component;

@Component
public class OrderAttachmentTransformer {

    public OrderAttachmentModel toModel(
            OrderAttachment attachment) {

        if (attachment == null) {
            return null;
        }

        return OrderAttachmentModel.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileUrl(attachment.getFileUrl())
                .contentType(attachment.getContentType())
                .fileSize(attachment.getFileSize())
                .attachmentType(attachment.getAttachmentType())
                .build();
    }

    public OrderAttachment toEntity(
            OrderAttachmentModel model) {

        if (model == null) {
            return null;
        }

        return OrderAttachment.builder()
                .id(model.getId())
                .fileName(model.getFileName())
                .fileUrl(model.getFileUrl())
                .contentType(model.getContentType())
                .fileSize(model.getFileSize())
                .attachmentType(model.getAttachmentType())
                .build();
    }
}
