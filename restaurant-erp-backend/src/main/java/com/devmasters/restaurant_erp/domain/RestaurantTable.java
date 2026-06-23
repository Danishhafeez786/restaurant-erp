package com.devmasters.restaurant_erp.domain;

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
@Document("tables")
public class RestaurantTable extends BaseEntity {
    private String tableNumber;
    private Integer capacity;
    private String qrToken;
    @DBRef
    private Branch branch;
}
