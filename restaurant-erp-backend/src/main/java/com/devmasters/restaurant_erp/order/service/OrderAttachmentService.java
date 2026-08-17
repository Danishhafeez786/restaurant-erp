package com.devmasters.restaurant_erp.order.service;

import com.devmasters.restaurant_erp.order.domain.OrderAttachment;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderAttachmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface OrderAttachmentService {

    OrderAttachment create(OrderAttachment entity, MultipartFile file);

    Page<OrderAttachment> search(OrderAttachmentSearchCriteria criteria, Pageable pageable);

    OrderAttachment findById(UUID id);

    OrderAttachment update(UUID id, OrderAttachment entity);

    OrderAttachment delete(UUID id);

    OrderAttachment restore(UUID id);
}