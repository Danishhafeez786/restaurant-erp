package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderTax;
import com.devmasters.restaurant_erp.model.order.OrderTaxModel;
import org.springframework.stereotype.Component;

@Component
public class OrderTaxTransformer {

    public OrderTaxModel toModel(OrderTax tax) {

        if (tax == null) {
            return null;
        }

        return OrderTaxModel.builder()
                .id(tax.getId())
                .taxCode(tax.getTaxCode())
                .taxName(tax.getTaxName())
                .taxType(tax.getTaxType())
                .taxValue(tax.getTaxValue())
                .taxAmount(tax.getTaxAmount())
                .build();
    }

    public OrderTax toEntity(OrderTaxModel model) {

        if (model == null) {
            return null;
        }

        return OrderTax.builder()
                .id(model.getId())
                .taxCode(model.getTaxCode())
                .taxName(model.getTaxName())
                .taxType(model.getTaxType())
                .taxValue(model.getTaxValue())
                .taxAmount(model.getTaxAmount())
                .build();
    }
}