package com.devmasters.restaurant_erp.domain;

import com.devmasters.restaurant_erp.enums.KitchenStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("kitchen_orders")
public class KitchenOrder extends BaseEntity {
    @DBRef
    private Order order;
    private KitchenStatus kitchenStatus;
    private Integer estimatedMinutes;
}
