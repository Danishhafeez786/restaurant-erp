package com.devmasters.restaurant_erp.common;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BigDecimalRange {

    private BigDecimal min;

    private BigDecimal max;
}
