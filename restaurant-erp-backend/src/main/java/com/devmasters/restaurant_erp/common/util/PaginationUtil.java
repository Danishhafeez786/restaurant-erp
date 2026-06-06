package com.devmasters.restaurant_erp.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationUtil {

    public static Pageable create(int page, int size) {
        return PageRequest.of(page, size);
    }
}
