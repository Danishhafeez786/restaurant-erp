package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.order.OrderPayment;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderPaymentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderPaymentService {

    boolean existsByPaymentNumberIgnoreCase(String paymentNumber, UUID organizationId);

    boolean existsByTransactionReferenceIgnoreCase(String transactionReference, UUID organizationId);

    boolean existsByPaymentNumberIgnoreCaseAndIdNot(String paymentNumber, UUID organizationId, UUID id);

    boolean existsByTransactionReferenceIgnoreCaseAndIdNot(String transactionReference, UUID organizationId, UUID id);

    boolean existsByOrderId(UUID orderId, UUID organizationId);

    OrderPayment create(OrderPayment entity);

    Page<OrderPayment> search(OrderPaymentSearchCriteria criteria, Pageable pageable);

    OrderPayment findById(UUID id);

    OrderPayment update(UUID id, OrderPayment entity);

    OrderPayment delete(UUID id);

    OrderPayment restore(UUID id);
}