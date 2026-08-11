package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.OrderPayment;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderPaymentSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements com.devmasters.restaurant_erp.service.OrderPaymentService {

    private final OrderPaymentRepository orderPaymentRepository;

    @Override
    public boolean existsByPaymentNumberIgnoreCase(String paymentNumber, UUID organizationId) {
        return orderPaymentRepository.existsByPaymentNumberIgnoreCaseAndOrganization_Id(paymentNumber, organizationId);
    }

    @Override
    public boolean existsByTransactionReferenceIgnoreCase(String transactionReference, UUID organizationId) {
        return orderPaymentRepository.existsByTransactionReferenceIgnoreCaseAndOrganization_Id(transactionReference, organizationId);
    }

    @Override
    public boolean existsByPaymentNumberIgnoreCaseAndIdNot(String paymentNumber, UUID organizationId, UUID id) {
        return orderPaymentRepository.existsByPaymentNumberIgnoreCaseAndOrganization_IdAndIdNot(paymentNumber, organizationId, id);
    }

    @Override
    public boolean existsByTransactionReferenceIgnoreCaseAndIdNot(String transactionReference, UUID organizationId, UUID id) {
        return orderPaymentRepository.existsByTransactionReferenceIgnoreCaseAndOrganization_IdAndIdNot(transactionReference, organizationId, id);
    }

    @Override
    public boolean existsByOrderId(UUID orderId, UUID organizationId) {
        return orderPaymentRepository.existsByOrderIdAndOrganization_Id(orderId, organizationId);
    }

    @Override
    public OrderPayment create(OrderPayment entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);
        return orderPaymentRepository.save(entity);
    }

    @Override
    public Page<OrderPayment> search(OrderPaymentSearchCriteria criteria, Pageable pageable) {
        return orderPaymentRepository.search(criteria, pageable);
    }

    @Override
    public OrderPayment findById(UUID id) {
        return orderPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order payment not found."));
    }

    @Override
    public OrderPayment update(UUID id, OrderPayment entity) {
        OrderPayment existing = findById(id);

        existing.setPaymentMethod(entity.getPaymentMethod());
        existing.setStatus(entity.getStatus());
        existing.setAmount(entity.getAmount());
        existing.setTransactionReference(entity.getTransactionReference());
        existing.setPaymentNote(entity.getPaymentNote());
        existing.setPaidAt(entity.getPaidAt());
        existing.setRefundedAt(entity.getRefundedAt());
        existing.setRefundReason(entity.getRefundReason());
        existing.setUpdatedAt(LocalDateTime.now());

        return orderPaymentRepository.save(existing);
    }

    @Override
    public OrderPayment delete(UUID id) {
        OrderPayment payment = findById(id);
        payment.setIsActive(false);
        payment.setUpdatedAt(LocalDateTime.now());
        return orderPaymentRepository.save(payment);
    }

    @Override
    public OrderPayment restore(UUID id) {
        OrderPayment payment = findById(id);
        payment.setIsActive(true);
        payment.setUpdatedAt(LocalDateTime.now());
        return orderPaymentRepository.save(payment);
    }
}