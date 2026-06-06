package com.devmasters.restaurant_erp.common.model.report;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportFilterModel {

    private LocalDate fromDate;

    private LocalDate toDate;

    private String branchId;
}
