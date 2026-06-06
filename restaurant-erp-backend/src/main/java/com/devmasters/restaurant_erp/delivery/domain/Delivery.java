package com.devmasters.restaurant_erp.delivery.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "deliveries")
public class Delivery extends BaseDomain {

    private String orderId;

    private String deliveryBoyId;

    private String status;

    private LocalDateTime pickupTime;

    private LocalDateTime deliveredTime;
}
