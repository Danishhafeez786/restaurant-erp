package com.devmasters.restaurant_erp.common.model.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesChartModel {

    private String label;

    private double value;
}
