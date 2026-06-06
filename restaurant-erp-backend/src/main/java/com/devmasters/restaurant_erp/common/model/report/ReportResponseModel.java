package com.devmasters.restaurant_erp.common.model.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponseModel {

    private String title;

    private Object data;

    private String generatedAt;
}