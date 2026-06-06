package com.devmasters.restaurant_erp.common.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponse {

    private boolean success;

    private String message;
}
