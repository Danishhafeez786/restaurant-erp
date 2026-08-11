package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderAttachment;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderAttachmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderAttachmentCustomRepository {

    Page<OrderAttachment> search(OrderAttachmentSearchCriteria criteria, Pageable pageable);
}