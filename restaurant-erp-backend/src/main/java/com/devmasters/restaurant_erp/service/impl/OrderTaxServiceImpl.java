package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.OrderTax;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderTaxSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderTaxRepository;
import com.devmasters.restaurant_erp.service.OrderTaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderTaxServiceImpl implements OrderTaxService {

    private final OrderTaxRepository orderTaxRepository;

    @Override
    public boolean existsByTaxNumberIgnoreCase(String taxNumber) {
        return orderTaxRepository.existsByTaxNumberIgnoreCase(taxNumber);
    }

    @Override
    public boolean existsByOrderAndTax(UUID orderId, UUID taxId) {
        return orderTaxRepository.existsByOrder_IdAndTax_Id(orderId, taxId);
    }

    @Override
    public boolean existsByOrderAndTaxAndIdNot(UUID orderId, UUID taxId, UUID id) {
        return orderTaxRepository.existsByOrder_IdAndTax_IdAndIdNot(orderId, taxId, id);
    }

    @Override
    public OrderTax create(OrderTax entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);
        return orderTaxRepository.save(entity);
    }

    @Override
    public Page<OrderTax> search(OrderTaxSearchCriteria criteria, Pageable pageable) {
        return orderTaxRepository.search(criteria, pageable);
    }

    @Override
    public OrderTax findById(UUID id) {
        return orderTaxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order tax not found."));
    }

    @Override
    public OrderTax update(UUID id, OrderTax entity) {
        OrderTax existing = findById(id);

        existing.setTaxName(entity.getTaxName());
        existing.setTaxRate(entity.getTaxRate());
        existing.setTaxableAmount(entity.getTaxableAmount());
        existing.setTaxAmount(entity.getTaxAmount());
        existing.setUpdatedAt(LocalDateTime.now());

        return orderTaxRepository.save(existing);
    }

    @Override
    public OrderTax delete(UUID id) {
        OrderTax existing = findById(id);
        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());
        return orderTaxRepository.save(existing);
    }

    @Override
    public OrderTax restore(UUID id) {
        OrderTax existing = findById(id);
        existing.setIsActive(true);
        existing.setUpdatedAt(LocalDateTime.now());
        return orderTaxRepository.save(existing);
    }
}