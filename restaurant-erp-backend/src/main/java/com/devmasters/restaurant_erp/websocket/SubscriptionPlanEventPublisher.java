package com.devmasters.restaurant_erp.websocket;

import com.devmasters.restaurant_erp.model.SubscriptionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionPlanEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void created(SubscriptionModel model) {

        messagingTemplate.convertAndSend(
                "/topic/subscription-plans",
                new SubscriptionPlanEvent(
                        "CREATED",
                        model
                )
        );
    }

    public void updated(SubscriptionModel model) {

        messagingTemplate.convertAndSend(
                "/topic/subscription-plans",
                new SubscriptionPlanEvent(
                        "UPDATED",
                        model
                )
        );
    }

    public void deleted(SubscriptionModel model) {

        messagingTemplate.convertAndSend(
                "/topic/subscription-plans",
                new SubscriptionPlanEvent(
                        "DELETED",
                        model
                )
        );
    }

    public void restored(SubscriptionModel model) {

        messagingTemplate.convertAndSend(
                "/topic/subscription-plans",
                new SubscriptionPlanEvent(
                        "RESTORED",
                        model
                )
        );
    }
}
