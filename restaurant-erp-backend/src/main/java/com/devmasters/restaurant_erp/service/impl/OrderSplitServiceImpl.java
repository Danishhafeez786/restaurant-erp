package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.order.Order;
import com.devmasters.restaurant_erp.domain.order.OrderItem;
import com.devmasters.restaurant_erp.domain.order.OrderSplit;
import com.devmasters.restaurant_erp.domain.order.OrderSplitItem;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderSplitSearchCriteria;
import com.devmasters.restaurant_erp.repository.OrderRepository;
import com.devmasters.restaurant_erp.repository.OrderSplitRepository;
import com.devmasters.restaurant_erp.service.OrderSplitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderSplitServiceImpl implements OrderSplitService {

    private final OrderSplitRepository orderSplitRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderSplit create(OrderSplit split) {
        validateSplit(split);

        UUID orderId = split.getOrder().getId();
        UUID organizationId = split.getOrganization().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found."));

        if (!order.getOrganization().getId().equals(organizationId)) {
            throw new RuntimeException(
                    "Order does not belong to this organization."
            );
        }

        if (orderSplitRepository
                .existsBySplitNumberIgnoreCaseAndOrganization_Id(
                        split.getSplitNumber(),
                        organizationId)) {
            throw new RuntimeException(
                    "Split number already exists."
            );
        }

        validateItemQuantities(
                split,
                order,
                organizationId
        );

        calculateAmounts(split, order);

        split.setPaid(false);
        split.setIsActive(true);
        split.setCreatedAt(LocalDateTime.now());

        return orderSplitRepository.save(split);
    }

    @Override
    public OrderSplit findById(UUID id) {
        return orderSplitRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order split not found."
                        ));
    }

    @Override
    public Page<OrderSplit> search(
            OrderSplitSearchCriteria criteria,
            Pageable pageable) {

        return orderSplitRepository.search(
                criteria,
                pageable
        );
    }

    @Override
    public List<OrderSplit> findByOrder(
            UUID orderId,
            UUID organizationId) {

        return orderSplitRepository
                .findByOrder_IdAndOrganization_Id(
                        orderId,
                        organizationId
                );
    }

    @Override
    public OrderSplit update(
            UUID id,
            OrderSplit split) {

        OrderSplit existing = findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException(
                    "Inactive split cannot be updated."
            );
        }

        if (Boolean.TRUE.equals(existing.getPaid())) {
            throw new RuntimeException(
                    "Paid split cannot be updated."
            );
        }

        existing.setNote(split.getNote());
        existing.setUpdatedAt(LocalDateTime.now());

        return orderSplitRepository.save(existing);
    }

    @Override
    public OrderSplit delete(UUID id) {

        OrderSplit existing = findById(id);

        if (Boolean.TRUE.equals(existing.getPaid())) {
            throw new RuntimeException(
                    "Paid split cannot be deleted."
            );
        }

        existing.setIsActive(false);
        existing.setUpdatedAt(LocalDateTime.now());

        return orderSplitRepository.save(existing);
    }

    @Override
    public OrderSplit restore(UUID id) {

        OrderSplit existing = findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive())) {
            throw new RuntimeException(
                    "Split is already active."
            );
        }

        existing.setIsActive(true);
        existing.setUpdatedAt(LocalDateTime.now());

        return orderSplitRepository.save(existing);
    }

    private void validateSplit(OrderSplit split) {

        if (split.getOrder() == null ||
                split.getOrder().getId() == null) {
            throw new RuntimeException(
                    "Order is required."
            );
        }

        if (split.getOrganization() == null ||
                split.getOrganization().getId() == null) {
            throw new RuntimeException(
                    "Organization is required."
            );
        }

        if (split.getItems() == null ||
                split.getItems().isEmpty()) {
            throw new RuntimeException(
                    "At least one item is required."
            );
        }
    }

    private void validateItemQuantities(
            OrderSplit split,
            Order order,
            UUID organizationId) {

        List<OrderSplit> existingSplits =
                orderSplitRepository
                        .findByOrder_IdAndOrganization_Id(
                                order.getId(),
                                organizationId
                        );

        Map<UUID, BigDecimal> allocated =
                new HashMap<>();

        for (OrderSplit existing : existingSplits) {

            if (!Boolean.TRUE.equals(
                    existing.getIsActive())) {
                continue;
            }

            if (existing.getItems() == null) {
                continue;
            }

            for (OrderSplitItem item :
                    existing.getItems()) {

                allocated.merge(
                        item.getOrderItemId(),
                        item.getQuantity(),
                        BigDecimal::add
                );
            }
        }

        for (OrderSplitItem splitItem :
                split.getItems()) {

            if (splitItem.getOrderItemId() == null) {
                throw new RuntimeException(
                        "Order item is required."
                );
            }

            if (splitItem.getQuantity() == null ||
                    splitItem.getQuantity()
                            .compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException(
                        "Split quantity must be greater than zero."
                );
            }

            OrderItem orderItem =
                    findOrderItem(
                            order,
                            splitItem.getOrderItemId()
                    );

            BigDecimal alreadyAllocated =
                    allocated.getOrDefault(
                            splitItem.getOrderItemId(),
                            BigDecimal.ZERO
                    );

            BigDecimal requested =
                    alreadyAllocated.add(
                            splitItem.getQuantity()
                    );

            if (requested.compareTo(orderItem.getQuantity()) > 0) {

                throw new RuntimeException(
                        "Split quantity exceeds " +
                                "available order item quantity."
                );
            }
        }
    }

    private OrderItem findOrderItem(
            Order order,
            UUID orderItemId) {

        return order.getItems()
                .stream()
                .filter(item ->
                        item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order item not found."
                        ));
    }

    private void calculateAmounts(
            OrderSplit split,
            Order order) {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderSplitItem splitItem :
                split.getItems()) {

            OrderItem orderItem =
                    findOrderItem(
                            order,
                            splitItem.getOrderItemId()
                    );

            BigDecimal unitPrice =
                    orderItem.getUnitPrice();

            splitItem.setUnitPrice(unitPrice);

            BigDecimal total =
                    unitPrice.multiply(
                            splitItem.getQuantity()
                    );

            splitItem.setTotalAmount(total);

            subtotal = subtotal.add(total);
        }

        BigDecimal discount =
                split.getDiscountAmount() != null
                        ? split.getDiscountAmount()
                        : BigDecimal.ZERO;

        BigDecimal tax =
                split.getTaxAmount() != null
                        ? split.getTaxAmount()
                        : BigDecimal.ZERO;

        split.setSubtotal(subtotal);

        split.setTotalAmount(
                subtotal
                        .subtract(discount)
                        .add(tax)
        );
    }
}