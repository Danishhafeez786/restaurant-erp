package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.order.OrderAttachment;
import com.devmasters.restaurant_erp.model.order.OrderAttachmentModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderAttachmentSearchCriteria;
import com.devmasters.restaurant_erp.service.OrderAttachmentService;
import com.devmasters.restaurant_erp.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.transformer.OrderAttachmentTransformer;
import com.devmasters.restaurant_erp.transformer.OrderTransformer;
import com.devmasters.restaurant_erp.transformer.OrganizationTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderAttachmentHandler {

    private final OrderAttachmentService orderAttachmentService;
    private final OrderAttachmentTransformer orderAttachmentTransformer;
    private final OrderTransformer orderTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    public OrderAttachmentModel create(OrderAttachmentModel model, MultipartFile file) {

        if (model == null) {
            throw new RuntimeException("Attachment data is required.");
        }

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Attachment file is required.");
        }

        if (model.getOrderModel() == null || model.getOrderModel().getId() == null) {

            throw new RuntimeException("Order is required.");
        }

        if (model.getOrganizationModel() == null || model.getOrganizationModel().getId() == null) {

            throw new RuntimeException("Organization is required.");
        }

        if (model.getBranchModel() == null || model.getBranchModel().getId() == null) {

            throw new RuntimeException("Branch is required.");
        }

        /*
         * Basic file validation.
         */
        validateFile(file);

        /*
         * Convert model → entity.
         */
        OrderAttachment entity = orderAttachmentTransformer.toEntity(model);

        /*
         * File metadata and physical storage
         * are handled by the service.
         */
        OrderAttachment saved = orderAttachmentService.create(entity, file);

        return orderAttachmentTransformer.toModel(saved);
    }

    public PageResponse<OrderAttachmentModel> getAll(OrderAttachmentSearchCriteria criteria, Pageable pageable) {

        Page<OrderAttachment> page = orderAttachmentService.search(criteria, pageable);

        return PageResponse.<OrderAttachmentModel>builder().content(orderAttachmentTransformer.toModels(page.getContent())).totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).page(page.getNumber()).size(page.getSize()).first(page.isFirst()).last(page.isLast()).build();
    }

    public OrderAttachmentModel getById(UUID id) {

        return orderAttachmentTransformer.toModel(orderAttachmentService.findById(id));
    }

    public OrderAttachmentModel update(UUID id, OrderAttachmentModel model) {

        OrderAttachment existing = orderAttachmentService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {

            throw new RuntimeException("Inactive attachment cannot be updated.");
        }

        /*
         * Don't allow changing the order,
         * organization or branch through update.
         */
        model.setOrderModel(orderTransformer.toModel(existing.getOrder()));
        model.setOrganizationModel(organizationTransformer.toModel(existing.getOrganization()));

        model.setBranchModel(branchTransformer.toModel(existing.getBranch()));

        OrderAttachment entity = orderAttachmentTransformer.toEntity(model);

        OrderAttachment updated = orderAttachmentService.update(id, entity);

        return orderAttachmentTransformer.toModel(updated);
    }

    public OrderAttachmentModel delete(UUID id) {

        OrderAttachment existing = orderAttachmentService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {

            throw new RuntimeException("Attachment is already inactive.");
        }

        return orderAttachmentTransformer.toModel(orderAttachmentService.delete(id));
    }

    public OrderAttachmentModel restore(UUID id) {

        OrderAttachment existing = orderAttachmentService.findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive())) {

            throw new RuntimeException("Attachment is already active.");
        }

        return orderAttachmentTransformer.toModel(orderAttachmentService.restore(id));
    }

    private void validateFile(MultipartFile file) {

        /*
         * 10 MB maximum.
         */
        long maxSize = 10 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size cannot exceed 10 MB.");
        }

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new RuntimeException("File type could not be determined.");
        }

        /*
         * Allowed file types.
         */
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("application/pdf") && !contentType.equals("image/webp")) {

            throw new RuntimeException("Unsupported file type.");
        }
    }
}