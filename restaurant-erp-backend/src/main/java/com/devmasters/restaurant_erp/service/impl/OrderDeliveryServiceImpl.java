package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDeliverySearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderDeliveryRepository;
import com.devmasters.restaurant_erp.service.OrderDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDeliveryServiceImpl implements OrderDeliveryService {

    private final OrderDeliveryRepository orderDeliveryRepository;

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return orderDeliveryRepository.existsByOrder_Id(orderId);
    }

    @Override
    public OrderDelivery create(OrderDelivery entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);

        if (entity.getStatus() == null)
            entity.setStatus(com.devmasters.restaurant_erp.enums.DeliveryStatus.PENDING);

        return orderDeliveryRepository.save(entity);
    }

    @Override
    public Page<OrderDelivery> search(OrderDeliverySearchCriteria criteria, Pageable pageable) {
        return orderDeliveryRepository.search(criteria, pageable);
    }

    @Override
    public OrderDelivery findById(UUID id) {
        return orderDeliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order delivery not found."));
    }

    @Override
    public OrderDelivery update(UUID id, OrderDelivery entity) {
        OrderDelivery existing = findById(id);

        existing.setDeliveryAddress(entity.getDeliveryAddress());
        existing.setDeliveryInstructions(entity.getDeliveryInstructions());
        existing.setDeliveryPartnerId(entity.getDeliveryPartnerId());
        existing.setStatus(entity.getStatus());
        existing.setAssignedAt(entity.getAssignedAt());
        existing.setPickedUpAt(entity.getPickedUpAt());
        existing.setDeliveredAt(entity.getDeliveredAt());
        existing.setCancelledAt(entity.getCancelledAt());
        existing.setCancellationReason(entity.getCancellationReason());
        existing.setAssignedBy(entity.getAssignedBy());
        existing.setUpdatedAt(LocalDateTime.now());

        return orderDeliveryRepository.save(existing);
    }

    @Override
    public OrderDelivery delete(UUID id) {
        OrderDelivery existing = findById(id);
        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());
        return orderDeliveryRepository.save(existing);
    }

    @Override
    public OrderDelivery restore(UUID id) {
        OrderDelivery existing = findById(id);
        existing.setIsActive(true);
        existing.setUpdatedAt(LocalDateTime.now());
        return orderDeliveryRepository.save(existing);
    }
}