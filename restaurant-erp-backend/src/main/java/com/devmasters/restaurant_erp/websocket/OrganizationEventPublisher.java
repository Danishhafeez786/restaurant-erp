package com.devmasters.restaurant_erp.websocket;

import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void created(OrganizationModel model) {

        messagingTemplate.convertAndSend(
                "/topic/organizations",
                new OrganizationEvent(
                        "CREATED",
                        model
                )
        );
    }

    public void updated(OrganizationModel model) {

        messagingTemplate.convertAndSend(
                "/topic/organizations",
                new OrganizationEvent(
                        "UPDATED",
                        model
                )
        );
    }

    public void deleted(OrganizationModel model) {

        messagingTemplate.convertAndSend(
                "/topic/organizations",
                new OrganizationEvent(
                        "DELETED",
                        model
                )
        );
    }

    public void restored(OrganizationModel model) {

        messagingTemplate.convertAndSend(
                "/topic/organizations",
                new OrganizationEvent(
                        "RESTORED",
                        model
                )
        );
    }
}
