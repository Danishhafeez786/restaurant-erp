package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.order.OrderRefund;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderRefundSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderRefundService {

    OrderRefund create(OrderRefund refund);
    OrderRefund findById(UUID id);
    Page<OrderRefund> search(OrderRefundSearchCriteria criteria, Pageable pageable);
    List<OrderRefund> findByOrder(UUID orderId, UUID organizationId);
    List<OrderRefund> findByOrderPayment(UUID orderPaymentId, UUID organizationId);
    OrderRefund update(UUID id, OrderRefund refund);
    OrderRefund delete(UUID id);
    OrderRefund restore(UUID id);
    OrderRefund processRefund(UUID id, UUID processedById);
}