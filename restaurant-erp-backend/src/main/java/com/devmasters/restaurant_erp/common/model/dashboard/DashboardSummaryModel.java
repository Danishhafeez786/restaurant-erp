package com.devmasters.restaurant_erp.common.model.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardSummaryModel {

    private double todaySales;

    private double monthlySales;

    private long totalOrders;

    private long totalCustomers;

    private long activeBranches;
}
