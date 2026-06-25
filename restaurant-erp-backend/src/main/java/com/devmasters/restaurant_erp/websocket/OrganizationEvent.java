package com.devmasters.restaurant_erp.websocket;

import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEvent {

    private String action;
    private OrganizationModel data;
}
