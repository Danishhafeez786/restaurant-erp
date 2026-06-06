package com.devmasters.restaurant_erp.common.model.system;

import com.devmasters.restaurant_erp.common.enums.FeatureFlag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureFlagModel {

    private FeatureFlag featureName;

    private boolean enabled;
}
