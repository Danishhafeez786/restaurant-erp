package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.model.order.OrderKitchenTicketModel;
import org.springframework.stereotype.Component;

@Component
public class OrderKitchenTicketTransformer {

    public OrderKitchenTicketModel toModel(
            OrderKitchenTicket kitchenTicket) {

        if (kitchenTicket == null) {
            return null;
        }

        return OrderKitchenTicketModel.builder()
                .id(kitchenTicket.getId())
                .ticketNumber(kitchenTicket.getTicketNumber())
                .status(kitchenTicket.getStatus())
                .kitchenStation(kitchenTicket.getKitchenStation())
                .sentAt(kitchenTicket.getSentAt())
                .startedAt(kitchenTicket.getStartedAt())
                .readyAt(kitchenTicket.getReadyAt())
                .note(kitchenTicket.getNote())
                .build();
    }

    public OrderKitchenTicket toEntity(
            OrderKitchenTicketModel model) {

        if (model == null) {
            return null;
        }

        return OrderKitchenTicket.builder()
                .id(model.getId())
                .ticketNumber(model.getTicketNumber())
                .status(model.getStatus())
                .kitchenStation(model.getKitchenStation())
                .sentAt(model.getSentAt())
                .startedAt(model.getStartedAt())
                .readyAt(model.getReadyAt())
                .note(model.getNote())
                .build();
    }
}