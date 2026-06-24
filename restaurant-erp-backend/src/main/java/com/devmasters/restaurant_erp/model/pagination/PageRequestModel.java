package com.devmasters.restaurant_erp.model.pagination;

import lombok.Data;

@Data
public class PageRequestModel {

    private int page = 0;

    private int size = 10;

    private String sortBy = "createdAt";

    private String sortDirection = "DESC";
}
