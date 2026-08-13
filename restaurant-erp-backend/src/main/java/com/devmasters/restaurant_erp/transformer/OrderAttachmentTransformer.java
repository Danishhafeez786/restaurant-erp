package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderAttachment;
import com.devmasters.restaurant_erp.model.order.OrderAttachmentModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderAttachmentTransformer extends Transformer<OrderAttachment, OrderAttachmentModel> {

    private final OrderTransformer orderTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public OrderAttachment toEntity(OrderAttachmentModel model) {

        if (model == null) return null;

        return OrderAttachment.builder().id(model.getId() != null ? model.getId() : UUID.randomUUID())

                .fileName(model.getFileName()).fileUrl(model.getFileUrl()).fileType(model.getFileType()).fileSize(model.getFileSize()).attachmentType(model.getAttachmentType()).description(model.getDescription())

                .order(orderTransformer.toEntity(model.getOrderModel()))

                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))

                .branch(branchTransformer.toEntity(model.getBranchModel()))

                .isActive(model.getIsActive()).createdAt(model.getCreatedAt()).updatedAt(model.getUpdatedAt())

                .build();
    }

    @Override
    public OrderAttachmentModel toModel(OrderAttachment entity) {

        if (entity == null) return null;

        return OrderAttachmentModel.builder().id(entity.getId())

                .fileName(entity.getFileName()).fileUrl(entity.getFileUrl()).fileType(entity.getFileType()).fileSize(entity.getFileSize()).attachmentType(entity.getAttachmentType()).description(entity.getDescription())

                .orderModel(orderTransformer.toModel(entity.getOrder()))

                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))

                .branchModel(branchTransformer.toModel(entity.getBranch()))

                .isActive(entity.getIsActive()).createdAt(entity.getCreatedAt()).updatedAt(entity.getUpdatedAt())

                .build();
    }

    public List<OrderAttachmentModel> toModels(List<OrderAttachment> entities) {

        if (entities == null) return null;

        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }

    public List<OrderAttachment> toEntities(List<OrderAttachmentModel> models) {

        if (models == null) return null;

        return models.stream().map(this::toEntity).collect(Collectors.toList());
    }
}