package com.devmasters.restaurant_erp.order.handler;

import com.devmasters.restaurant_erp.order.domain.Order;
import com.devmasters.restaurant_erp.common.enums.OrderStatus;
import com.devmasters.restaurant_erp.order.model.OrderItemModel;
import com.devmasters.restaurant_erp.order.model.OrderItemModifierModel;
import com.devmasters.restaurant_erp.order.model.OrderModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSearchCriteria;
import com.devmasters.restaurant_erp.order.service.OrderService;
import com.devmasters.restaurant_erp.common.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.order.transformer.OrderTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderHandler {

    private final OrderService orderService;
    private final OrderTransformer orderTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public OrderModel create(OrderModel model) {
        validateCreate(model);

        UUID branchId = model.getBranchModel().getId();
        model.setOrderNumber(codeGeneratorService.generateOrderNumber(branchId));

        Order entity = orderTransformer.toEntity(model);
        Order saved = orderService.create(entity);

        return orderTransformer.toModel(saved);
    }

    public PageResponse<OrderModel> getAll(OrderSearchCriteria criteria, Pageable pageable) {
        Page<Order> page = orderService.search(criteria, pageable);

        return PageResponse.<OrderModel>builder()
                .content(orderTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public OrderModel getById(UUID id) {
        Order order = getExistingOrder(id);
        return orderTransformer.toModel(order);
    }

    public OrderModel update(UUID id, OrderModel model) {
        if (id == null) {
            throw new RuntimeException("Order ID is required.");
        }

        validateUpdate(model);

        Order existing = orderService.findById(id);

        if (existing.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Completed order cannot be updated.");
        }

        if (existing.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cancelled order cannot be updated.");
        }

        Order entity = orderTransformer.toEntity(model);
        Order updated = orderService.update(id, entity);

        return orderTransformer.toModel(updated);
    }

    public OrderModel confirm(UUID id) {
        Order order = getExistingOrder(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be confirmed.");
        }

        return orderTransformer.toModel(orderService.confirm(id));
    }

    public OrderModel startPreparing(UUID id) {
        Order order = getExistingOrder(id);

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed orders can start preparing.");
        }

        return orderTransformer.toModel(orderService.startPreparing(id));
    }

    public OrderModel markReady(UUID id) {
        Order order = getExistingOrder(id);

        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new RuntimeException("Only preparing orders can be marked ready.");
        }

        return orderTransformer.toModel(orderService.markReady(id));
    }

    public OrderModel complete(UUID id) {
        Order order = getExistingOrder(id);

        if (order.getStatus() != OrderStatus.READY) {
            throw new RuntimeException("Only ready orders can be completed.");
        }

        return orderTransformer.toModel(orderService.complete(id));
    }

    public OrderModel cancel(UUID id, String cancellationReason) {
        Order order = getExistingOrder(id);

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Completed order cannot be cancelled.");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled.");
        }

        if (!StringUtils.hasText(cancellationReason)) {
            throw new RuntimeException("Cancellation reason is required.");
        }

        return orderTransformer.toModel(
                orderService.cancel(id, cancellationReason)
        );
    }

    private void validateCreate(OrderModel model) {
        if (model == null) {
            throw new RuntimeException("Order data is required.");
        }

        validateOrganizationAndBranch(model);
        validateOrderDetails(model);
        validateItems(model.getItems());
        validateAmounts(model);
    }

    private void validateUpdate(OrderModel model) {
        if (model == null) {
            throw new RuntimeException("Order data is required.");
        }

        validateOrganizationAndBranch(model);
        validateOrderDetails(model);
        validateItems(model.getItems());
        validateAmounts(model);
    }

    private void validateOrganizationAndBranch(OrderModel model) {
        if (model.getOrganizationModel() == null ||
                model.getOrganizationModel().getId() == null) {
            throw new RuntimeException("Organization is required.");
        }

        if (model.getBranchModel() == null ||
                model.getBranchModel().getId() == null) {
            throw new RuntimeException("Branch is required.");
        }
    }

    private void validateOrderDetails(OrderModel model) {
        if (model.getOrderType() == null) {
            throw new RuntimeException("Order type is required.");
        }

        if (model.getOrderSource() == null) {
            throw new RuntimeException("Order source is required.");
        }

        if (model.getPersons() != null && model.getPersons() <= 0) {
            throw new RuntimeException("Persons must be greater than zero.");
        }
    }

    private void validateItems(List<OrderItemModel> items) {
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Order must contain at least one item.");
        }

        for (OrderItemModel item : items) {
            if (item == null) {
                throw new RuntimeException("Order item cannot be null.");
            }

            if (item.getMenuItemId() == null) {
                throw new RuntimeException("Menu item is required.");
            }

            if (!StringUtils.hasText(item.getItemName())) {
                throw new RuntimeException("Item name is required.");
            }

            if (item.getQuantity() == null || item.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Item quantity must be greater than zero.");
            }

            if (item.getUnitPrice() == null ||
                    item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Item price cannot be negative.");
            }

            validateModifiers(item.getModifiers());
        }
    }

    private void validateModifiers(List<OrderItemModifierModel> modifiers) {
        if (modifiers == null) {
            return;
        }

        for (OrderItemModifierModel modifier : modifiers) {
            if (modifier == null) {
                throw new RuntimeException("Order item modifier cannot be null.");
            }

            if (modifier.getModifierId() == null) {
                throw new RuntimeException("Modifier is required.");
            }

            if (!StringUtils.hasText(modifier.getModifierName())) {
                throw new RuntimeException("Modifier name is required.");
            }

            if (modifier.getQuantity() == null || modifier.getQuantity() <= 0) {
                throw new RuntimeException("Modifier quantity must be greater than zero.");
            }

            if (modifier.getUnitPrice() == null ||
                    modifier.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Modifier price cannot be negative.");
            }
        }
    }

    private void validateAmounts(OrderModel model) {
        if (model.getSubtotalAmount() != null &&
                model.getSubtotalAmount().signum() < 0) {
            throw new RuntimeException("Subtotal amount cannot be negative.");
        }

        if (model.getDiscountAmount() != null &&
                model.getDiscountAmount().signum() < 0) {
            throw new RuntimeException("Discount amount cannot be negative.");
        }

        if (model.getTaxAmount() != null &&
                model.getTaxAmount().signum() < 0) {
            throw new RuntimeException("Tax amount cannot be negative.");
        }

        if (model.getServiceChargeAmount() != null &&
                model.getServiceChargeAmount().signum() < 0) {
            throw new RuntimeException("Service charge cannot be negative.");
        }

        if (model.getDeliveryChargeAmount() != null &&
                model.getDeliveryChargeAmount().signum() < 0) {
            throw new RuntimeException("Delivery charge cannot be negative.");
        }

        if (model.getRoundingAmount() != null &&
                model.getRoundingAmount().signum() < 0) {
            throw new RuntimeException("Rounding amount cannot be negative.");
        }

        if (model.getTotalAmount() != null &&
                model.getTotalAmount().signum() < 0) {
            throw new RuntimeException("Total amount cannot be negative.");
        }
    }

    private Order getExistingOrder(UUID id) {
        if (id == null) {
            throw new RuntimeException("Order ID is required.");
        }

        return orderService.findById(id);
    }
}
