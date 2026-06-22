package com.devmasters.restaurant_erp.model.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private int pageNumber;
    private int pageSize;

    private long totalElements;
    private int totalPages;

    private boolean hasNext;
    private boolean hasPrevious;
}
