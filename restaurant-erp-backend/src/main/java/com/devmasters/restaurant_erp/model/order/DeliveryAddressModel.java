package com.devmasters.restaurant_erp.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressModel {

    private String receiverName;

    private String phoneNumber;

    private String address;

    private String area;

    private String city;

    private String state;

    private String postalCode;

    private String deliveryInstructions;
}
