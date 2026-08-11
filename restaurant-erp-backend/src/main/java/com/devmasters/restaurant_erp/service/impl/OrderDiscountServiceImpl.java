package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDiscountSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDiscountServiceImpl implements com.devmasters.restaurant_erp.service.order.OrderDiscountService {

    private final OrderDiscountRepository orderDiscountRepository;

    @Override
    public boolean existsByDiscountNumberIgnoreCase(String discountNumber, UUID organizationId) {
        return orderDiscountRepository.existsByDiscountNumberIgnoreCaseAndOrganization_IdAndIsActiveTrue(discountNumber, organizationId);
    }

    @Override
    public boolean existsByDiscountNameIgnoreCase(String discountName, UUID organizationId) {
        return orderDiscountRepository.existsByDiscountNameIgnoreCaseAndOrganization_IdAndIsActiveTrue(discountName, organizationId);
    }

    @Override
    public boolean existsByDiscountNumberIgnoreCaseAndIdNot(String discountNumber, UUID organizationId, UUID id) {
        return orderDiscountRepository.existsByDiscountNumberIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
                discountNumber, organizationId, id);
    }

    @Override
    public boolean existsByDiscountNameIgnoreCaseAndIdNot(String discountName, UUID organizationId, UUID id) {
        return orderDiscountRepository.existsByDiscountNameIgnoreCaseAndOrganization_IdAndIsActiveTrueAndIdNot(
                discountName, organizationId, id);
    }

    @Override
    public boolean existsByOrderId(UUID orderId, UUID organizationId) {
        return orderDiscountRepository.existsByOrderIdAndOrganization_IdAndIsActiveTrue(orderId, organizationId);
    }

    @Override
    public OrderDiscount create(OrderDiscount entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);
        return orderDiscountRepository.save(entity);
    }

    @Override
    public Page<OrderDiscount> search(OrderDiscountSearchCriteria criteria, Pageable pageable) {
        return orderDiscountRepository.search(criteria, pageable);
    }

    @Override
    public OrderDiscount findById(UUID id) {
        return orderDiscountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order discount not found."));
    }

    @Override
    public OrderDiscount update(UUID id, OrderDiscount entity) {
        OrderDiscount existing = findById(id);

        existing.setDiscountName(entity.getDiscountName());
        existing.setDiscountType(entity.getDiscountType());
        existing.setDiscountValue(entity.getDiscountValue());
        existing.setDiscountAmount(entity.getDiscountAmount());
        existing.setReason(entity.getReason());
        existing.setUpdatedAt(LocalDateTime.now());

        return orderDiscountRepository.save(existing);
    }

    @Override
    public OrderDiscount delete(UUID id) {
        OrderDiscount discount = findById(id);
        discount.setIsActive(false);
        discount.setUpdatedAt(LocalDateTime.now());
        return orderDiscountRepository.save(discount);
    }

    @Override
    public OrderDiscount restore(UUID id) {
        OrderDiscount discount = findById(id);
        discount.setIsActive(true);
        discount.setUpdatedAt(LocalDateTime.now());
        return orderDiscountRepository.save(discount);
    }
}