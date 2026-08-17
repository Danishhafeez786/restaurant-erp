package com.devmasters.restaurant_erp.order.service.impl;

import com.devmasters.restaurant_erp.order.domain.OrderDiscount;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderDiscountSearchCriteria;
import com.devmasters.restaurant_erp.order.respository.OrderDiscountRepository;
import com.devmasters.restaurant_erp.order.service.OrderDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDiscountServiceImpl implements OrderDiscountService {

    private final OrderDiscountRepository orderDiscountRepository;

    @Override
    public boolean existsByDiscountNumberIgnoreCase(String discountNumber) {
        return orderDiscountRepository.existsByDiscountNumberIgnoreCase(discountNumber);
    }

    @Override
    public boolean existsByOrderAndDiscountName(UUID orderId, String discountName) {
        return orderDiscountRepository.existsByOrder_IdAndDiscountNameIgnoreCase(orderId, discountName);
    }

    @Override
    public boolean existsByOrderAndDiscountNameAndIdNot(UUID orderId, String discountName, UUID id) {
        return orderDiscountRepository.existsByOrder_IdAndDiscountNameIgnoreCaseAndIdNot(orderId, discountName, id);
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
        existing.setTaxableAmount(entity.getTaxableAmount());
        existing.setUpdatedAt(LocalDateTime.now());

        return orderDiscountRepository.save(existing);
    }

    @Override
    public OrderDiscount delete(UUID id) {
        OrderDiscount existing = findById(id);
        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());
        return orderDiscountRepository.save(existing);
    }

    @Override
    public OrderDiscount restore(UUID id) {
        OrderDiscount existing = findById(id);
        existing.setIsActive(true);
        existing.setUpdatedAt(LocalDateTime.now());
        return orderDiscountRepository.save(existing);
    }
}