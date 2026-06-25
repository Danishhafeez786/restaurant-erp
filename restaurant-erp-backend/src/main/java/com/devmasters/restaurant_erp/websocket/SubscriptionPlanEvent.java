package com.devmasters.restaurant_erp.websocket;

import com.devmasters.restaurant_erp.model.SubscriptionModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanEvent {

    private String action;

    private SubscriptionModel data;
}