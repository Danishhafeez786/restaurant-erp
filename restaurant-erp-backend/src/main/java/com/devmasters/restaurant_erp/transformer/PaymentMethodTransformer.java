package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.PaymentMethod;
import com.devmasters.restaurant_erp.model.PaymentMethodModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentMethodTransformer extends Transformer<PaymentMethod, PaymentMethodModel> {

    private final OrganizationTransformer organizationTransformer;

    @Override
    public PaymentMethod toEntity(PaymentMethodModel model) {

        if (model == null)
            return null;

        return PaymentMethod.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .methodName(model.getMethodName())
                .code(model.getCode())
                .description(model.getDescription())
                .online(model.getOnline())
                .cashBased(model.getCashBased())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public PaymentMethodModel toModel(PaymentMethod entity) {

        if (entity == null)
            return null;

        return PaymentMethodModel.builder()
                .id(entity.getId())
                .methodName(entity.getMethodName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .online(entity.getOnline())
                .cashBased(entity.getCashBased())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
