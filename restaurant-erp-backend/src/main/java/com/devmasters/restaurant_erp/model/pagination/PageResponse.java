package com.devmasters.restaurant_erp.model.pagination;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse<T> {

    private List<T> content;

    private long totalElements;

    private int totalPages;

    private int page;

    private int size;

    private boolean first;

    private boolean last;
}
