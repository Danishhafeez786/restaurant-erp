package com.devmasters.restaurant_erp.common.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private boolean success;

    private String errorCode;

    private String message;

    private long timestamp;
}
