package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.Order;
import com.devmasters.restaurant_erp.enums.OrderStatus;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderRepository;
import com.devmasters.restaurant_erp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    @Override
    public Order create(Order entity) {

        entity.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(entity);
    }


    @Override
    public Page<Order> search(OrderSearchCriteria criteria, Pageable pageable) {

        return orderRepository.search(criteria, pageable);
    }


    @Override
    public Order findById(UUID id) {

        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found."));
    }


    @Override
    public Order update(UUID id, Order entity) {

        Order existing = findById(id);

        existing.setOrderType(entity.getOrderType());

        existing.setOrderSource(entity.getOrderSource());

        existing.setPersons(entity.getPersons());

        existing.setSubtotalAmount(entity.getSubtotalAmount());

        existing.setDiscountAmount(entity.getDiscountAmount());

        existing.setTaxAmount(entity.getTaxAmount());

        existing.setServiceChargeAmount(entity.getServiceChargeAmount());

        existing.setDeliveryChargeAmount(entity.getDeliveryChargeAmount());

        existing.setRoundingAmount(entity.getRoundingAmount());

        existing.setTotalAmount(entity.getTotalAmount());

        existing.setCustomerNote(entity.getCustomerNote());

        existing.setInternalNote(entity.getInternalNote());

        existing.setCancellationReason(entity.getCancellationReason());

        existing.setTableSessionNumber(entity.getTableSessionNumber());

        existing.setOrganization(entity.getOrganization());

        existing.setBranch(entity.getBranch());

        existing.setCustomer(entity.getCustomer());

        existing.setRestaurantTable(entity.getRestaurantTable());

        existing.setItems(entity.getItems());

        existing.setUpdatedBy(entity.getUpdatedBy());

        return orderRepository.save(existing);
    }


    @Override
    public Order confirm(UUID id) {

        Order order = findById(id);

        order.setStatus(OrderStatus.CONFIRMED);

        order.setConfirmedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }


    @Override
    public Order startPreparing(UUID id) {

        Order order = findById(id);

        order.setStatus(OrderStatus.PREPARING);

        order.setPreparingAt(LocalDateTime.now());

        return orderRepository.save(order);
    }


    @Override
    public Order markReady(UUID id) {

        Order order = findById(id);

        order.setStatus(OrderStatus.READY);

        order.setReadyAt(LocalDateTime.now());

        return orderRepository.save(order);
    }


    @Override
    public Order complete(UUID id) {

        Order order = findById(id);

        order.setStatus(OrderStatus.COMPLETED);

        order.setCompletedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }


    @Override
    public Order cancel(UUID id, String cancellationReason) {

        Order order = findById(id);

        order.setStatus(OrderStatus.CANCELLED);

        order.setCancellationReason(cancellationReason);

        order.setCancelledAt(LocalDateTime.now());

        return orderRepository.save(order);
    }
}