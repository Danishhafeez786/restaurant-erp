package com.devmasters.restaurant_erp.model.searchcriteria;


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
