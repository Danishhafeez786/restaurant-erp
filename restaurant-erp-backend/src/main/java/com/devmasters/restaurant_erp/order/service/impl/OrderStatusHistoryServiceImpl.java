package com.devmasters.restaurant_erp.order.service.impl;

import com.devmasters.restaurant_erp.order.domain.OrderStatusHistory;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderStatusHistorySearchCriteria;
import com.devmasters.restaurant_erp.order.respository.OrderStatusHistoryRepository;
import com.devmasters.restaurant_erp.order.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Override
    public OrderStatusHistory create(OrderStatusHistory entity) {
        entity.setChangedAt(
                entity.getChangedAt() != null
                        ? entity.getChangedAt()
                        : LocalDateTime.now()
        );
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);

        return orderStatusHistoryRepository.save(entity);
    }

    @Override
    public Page<OrderStatusHistory> search(OrderStatusHistorySearchCriteria criteria, Pageable pageable) {
        return orderStatusHistoryRepository.search(criteria, pageable);
    }

    @Override
    public OrderStatusHistory findById(UUID id) {
        return orderStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order status history not found."));
    }
}