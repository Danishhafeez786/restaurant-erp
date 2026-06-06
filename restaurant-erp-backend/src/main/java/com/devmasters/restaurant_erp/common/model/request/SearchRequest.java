package com.devmasters.restaurant_erp.common.model.request;

import lombok.Data;

@Data
public class SearchRequest {

    private String keyword;

    private int page = 0;

    private int size = 10;
}
