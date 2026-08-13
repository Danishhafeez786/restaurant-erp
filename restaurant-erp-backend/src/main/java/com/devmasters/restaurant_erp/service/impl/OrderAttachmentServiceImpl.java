package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.OrderAttachment;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderAttachmentSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderAttachmentRepository;
import com.devmasters.restaurant_erp.service.OrderAttachmentService;
import com.devmasters.restaurant_erp.service.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderAttachmentServiceImpl implements OrderAttachmentService {

    private final OrderAttachmentRepository orderAttachmentRepository;
    private final FileStorageService fileStorageService;

    @Override
    public OrderAttachment create(OrderAttachment entity, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required.");
        }

        if (entity.getOrder() == null || entity.getOrder().getId() == null) {

            throw new RuntimeException("Order is required.");
        }

        /*
         * Store file first.
         */
        String folder = "orders/" + entity.getOrder().getId();

        String fileUrl = fileStorageService.upload(file, folder);

        /*
         * Store file metadata in MongoDB.
         */
        entity.setFileName(file.getOriginalFilename());

        entity.setFileUrl(fileUrl);

        entity.setFileType(file.getContentType());

        entity.setFileSize(file.getSize());

        entity.setCreatedAt(LocalDateTime.now());

        entity.setIsActive(true);

        return orderAttachmentRepository.save(entity);
    }

    @Override
    public Page<OrderAttachment> search(OrderAttachmentSearchCriteria criteria, Pageable pageable) {

        return orderAttachmentRepository.search(criteria, pageable);
    }

    @Override
    public OrderAttachment findById(UUID id) {

        return orderAttachmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Order attachment not found."));
    }

    @Override
    public OrderAttachment update(UUID id, OrderAttachment entity) {

        OrderAttachment existing = findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {

            throw new RuntimeException("Inactive attachment cannot be updated.");
        }

        /*
         * Metadata only.
         *
         * The actual file is not replaced here.
         */
        existing.setAttachmentType(entity.getAttachmentType());

        existing.setDescription(entity.getDescription());

        existing.setUpdatedAt(LocalDateTime.now());

        return orderAttachmentRepository.save(existing);
    }

    @Override
    public OrderAttachment delete(UUID id) {

        OrderAttachment existing = findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {

            throw new RuntimeException("Attachment is already inactive.");
        }

        /*
         * Soft delete.
         *
         * We intentionally DO NOT delete
         * the physical file here.
         */
        existing.setIsActive(false);

        existing.setUpdatedAt(LocalDateTime.now());

        return orderAttachmentRepository.save(existing);
    }

    @Override
    public OrderAttachment restore(UUID id) {

        OrderAttachment existing = findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive())) {

            throw new RuntimeException("Attachment is already active.");
        }

        existing.setIsActive(true);

        existing.setUpdatedAt(LocalDateTime.now());

        return orderAttachmentRepository.save(existing);
    }
}