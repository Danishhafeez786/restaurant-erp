package com.devmasters.restaurant_erp.common.model.websocket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocketMessageModel {

    private String type; // ORDER_CREATED, ORDER_UPDATED

    private String tenantId;

    private String branchId;

    private Object payload;
}
