package com.devmasters.restaurant_erp.kitchenOrder.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import com.devmasters.restaurant_erp.common.enums.KitchenStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "kitchen_orders")
public class KitchenOrder extends BaseDomain {

    private String orderId;

    private String chefId;

    private KitchenStatus kitchenStatus;

    private Long preparationTime;
}
