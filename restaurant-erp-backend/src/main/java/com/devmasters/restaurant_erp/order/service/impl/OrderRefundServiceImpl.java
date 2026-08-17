package com.devmasters.restaurant_erp.order.service.impl;

import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.order.domain.OrderPayment;
import com.devmasters.restaurant_erp.order.domain.OrderRefund;
import com.devmasters.restaurant_erp.common.enums.RefundStatus;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderRefundSearchCriteria;
import com.devmasters.restaurant_erp.order.respository.OrderRefundRepository;
import com.devmasters.restaurant_erp.order.service.OrderRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderRefundServiceImpl implements OrderRefundService {

    private final OrderRefundRepository orderRefundRepository;

    @Override
    public OrderRefund create(OrderRefund refund) {
        if (refund.getRefundAmount() == null || refund.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Refund amount must be greater than zero.");
        }

        if (refund.getOrder() == null || refund.getOrder().getId() == null) {
            throw new RuntimeException("Order is required.");
        }

        if (refund.getOrderPayment() == null || refund.getOrderPayment().getId() == null) {
            throw new RuntimeException("Order payment is required.");
        }

        if (refund.getOrganization() == null || refund.getOrganization().getId() == null) {
            throw new RuntimeException("Organization is required.");
        }

        UUID organizationId = refund.getOrganization().getId();

        if (orderRefundRepository.existsByRefundNumberIgnoreCaseAndOrganization_Id(refund.getRefundNumber(), organizationId)) {
            throw new RuntimeException("Refund number already exists.");
        }

        refund.setStatus(RefundStatus.PENDING);
        refund.setRequestedAt(LocalDateTime.now());
        refund.setIsActive(true);

        return orderRefundRepository.save(refund);
    }

    @Override
    public OrderRefund findById(UUID id) {
        return orderRefundRepository.findById(id).orElseThrow(() -> new RuntimeException("Order refund not found."));
    }

    @Override
    public Page<OrderRefund> search(OrderRefundSearchCriteria criteria, Pageable pageable) {
        return orderRefundRepository.search(criteria, pageable);
    }

    @Override
    public List<OrderRefund> findByOrder(UUID orderId, UUID organizationId) {
        return orderRefundRepository.findByOrder_IdAndOrganization_Id(orderId, organizationId);
    }

    @Override
    public List<OrderRefund> findByOrderPayment(UUID orderPaymentId, UUID organizationId) {
        return orderRefundRepository.findByOrderPayment_IdAndOrganization_Id(orderPaymentId, organizationId);
    }

    @Override
    public OrderRefund update(UUID id, OrderRefund refund) {

        OrderRefund existing = findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException("Inactive refund cannot be updated.");
        }

        if (existing.getStatus() == RefundStatus.COMPLETED) {
            throw new RuntimeException("Completed refund cannot be updated.");
        }

        existing.setRefundAmount(refund.getRefundAmount());
        existing.setReason(refund.getReason());
        existing.setNote(refund.getNote());
        existing.setTransactionReference(refund.getTransactionReference());

        existing.setUpdatedAt(LocalDateTime.now());

        return orderRefundRepository.save(existing);
    }

    @Override
    public OrderRefund delete(UUID id) {

        OrderRefund existing = findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException("Refund is already inactive.");
        }

        if (existing.getStatus() == RefundStatus.COMPLETED) {
            throw new RuntimeException("Completed refund cannot be deleted.");
        }

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        return orderRefundRepository.save(existing);
    }

    @Override
    public OrderRefund restore(UUID id) {

        OrderRefund existing = findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException("Refund is already active.");
        }

        existing.setIsActive(true);
        existing.setUpdatedAt(LocalDateTime.now());

        return orderRefundRepository.save(existing);
    }

    @Override
    public OrderRefund processRefund(UUID id, UUID processedById) {
        OrderRefund refund = findById(id);

        if (!Boolean.TRUE.equals(refund.getIsActive())) {
            throw new RuntimeException("Inactive refund cannot be processed.");
        }

        if (refund.getStatus() != RefundStatus.PENDING) {
            throw new RuntimeException("Only pending refunds can be processed.");
        }

        OrderPayment payment = refund.getOrderPayment();

        if (payment == null || payment.getId() == null) {
            throw new RuntimeException("Order payment is required.");
        }

        UUID organizationId = refund.getOrganization().getId();

        List<OrderRefund> refunds =
                orderRefundRepository.findByOrderPayment_IdAndOrganization_Id(
                        payment.getId(),
                        organizationId
                );

        BigDecimal alreadyRefunded = refunds.stream()
                .filter(r -> r.getId() != null &&
                        !r.getId().equals(refund.getId()))
                .filter(r -> r.getStatus() == RefundStatus.COMPLETED)
                .map(OrderRefund::getRefundAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paymentAmount = payment.getAmount();

        BigDecimal remainingAmount =
                paymentAmount.subtract(alreadyRefunded);

        if (refund.getRefundAmount().compareTo(remainingAmount) > 0) {
            throw new RuntimeException(
                    "Refund amount exceeds remaining refundable amount. Remaining: "
                            + remainingAmount
            );
        }

        refund.setStatus(RefundStatus.COMPLETED);
        refund.setProcessedAt(LocalDateTime.now());

        if (processedById != null) {
            Employee employee = new Employee();
            employee.setId(processedById);
            refund.setProcessedBy(employee);
        }

        refund.setUpdatedAt(LocalDateTime.now());

        return orderRefundRepository.save(refund);
    }
}