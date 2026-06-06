package com.devmasters.restaurant_erp.common.model.system;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthResponseModel {

    private String status;

    private String version;

    private long uptime;
}
