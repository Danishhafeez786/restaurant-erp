package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderAttachment;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderAttachmentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderAttachmentCustomRepository {

    Page<OrderAttachment> search(OrderAttachmentSearchCriteria criteria, Pageable pageable);
}