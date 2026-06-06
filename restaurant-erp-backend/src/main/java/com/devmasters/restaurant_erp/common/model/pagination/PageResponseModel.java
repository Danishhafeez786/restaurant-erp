package com.devmasters.restaurant_erp.common.model.pagination;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseModel<T> {

    private List<T> content;

    private long totalElements;

    private int totalPages;

    private int page;

    private int size;
}