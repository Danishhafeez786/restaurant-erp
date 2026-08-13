package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.enums.KitchenTicketStatus;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderKitchenTicketSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderKitchenTicketRepository;
import com.devmasters.restaurant_erp.service.OrderKitchenTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderKitchenTicketServiceImpl implements OrderKitchenTicketService {

    private final OrderKitchenTicketRepository orderKitchenTicketRepository;

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return orderKitchenTicketRepository.existsByOrder_Id(orderId);
    }

    @Override
    public OrderKitchenTicket create(OrderKitchenTicket entity) {

        entity.setCreatedAt(LocalDateTime.now());
        entity.setIsActive(true);

        if (entity.getStatus() == null) {
            entity.setStatus(KitchenTicketStatus.PENDING);
        }

        if (entity.getPriority() == null) {
            entity.setPriority(0);
        }

        if (entity.getSentAt() == null) {
            entity.setSentAt(LocalDateTime.now());
        }

        return orderKitchenTicketRepository.save(entity);
    }

    @Override
    public Page<OrderKitchenTicket> search(
            OrderKitchenTicketSearchCriteria criteria,
            Pageable pageable) {

        return orderKitchenTicketRepository.search(
                criteria,
                pageable
        );
    }

    @Override
    public OrderKitchenTicket findById(UUID id) {

        return orderKitchenTicketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order kitchen ticket not found."
                        )
                );
    }

    @Override
    public OrderKitchenTicket update(
            UUID id,
            OrderKitchenTicket entity) {

        OrderKitchenTicket existing = findById(id);

        existing.setPriority(entity.getPriority());
        existing.setKitchenNote(entity.getKitchenNote());
        existing.setStatus(entity.getStatus());
        existing.setAssignedTo(entity.getAssignedTo());

        existing.setAcceptedAt(entity.getAcceptedAt());
        existing.setPreparingAt(entity.getPreparingAt());
        existing.setReadyAt(entity.getReadyAt());
        existing.setCompletedAt(entity.getCompletedAt());

        existing.setCancelledAt(entity.getCancelledAt());
        existing.setCancellationReason(
                entity.getCancellationReason()
        );

        existing.setUpdatedAt(LocalDateTime.now());

        return orderKitchenTicketRepository.save(existing);
    }

    @Override
    public OrderKitchenTicket delete(UUID id) {

        OrderKitchenTicket existing = findById(id);

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        return orderKitchenTicketRepository.save(existing);
    }

    @Override
    public OrderKitchenTicket restore(UUID id) {

        OrderKitchenTicket existing = findById(id);

        existing.setIsActive(true);
        existing.setUpdatedAt(LocalDateTime.now());

        return orderKitchenTicketRepository.save(existing);
    }
}