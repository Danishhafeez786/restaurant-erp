package com.devmasters.restaurant_erp.common.model.websocket;

import com.devmasters.restaurant_erp.common.enums.KitchenStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KitchenSocketModel {

    private String orderId;

    private KitchenStatus status;

    private String assignedChefId;

    private long estimatedTime;
}
