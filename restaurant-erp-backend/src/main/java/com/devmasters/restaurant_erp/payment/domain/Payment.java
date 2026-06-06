package com.devmasters.restaurant_erp.payment.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import com.devmasters.restaurant_erp.common.enums.PaymentMethod;
import com.devmasters.restaurant_erp.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment extends BaseDomain {

    private String orderId;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private Double amount;
}
