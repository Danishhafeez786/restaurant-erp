package com.devmasters.restaurant_erp.common.model.request;

import lombok.Data;

@Data
public class ApiRequest<T> {

    private T data;

    private String requestId;

    private String source; // WEB, MOBILE, TABLET
}