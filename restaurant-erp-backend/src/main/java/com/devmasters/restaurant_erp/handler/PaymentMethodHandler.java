package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.PaymentMethod;
import com.devmasters.restaurant_erp.model.PaymentMethodModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.PaymentMethodSearchCriteria;
import com.devmasters.restaurant_erp.service.PaymentMethodService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.PaymentMethodTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentMethodHandler {

    private final PaymentMethodService service;
    private final PaymentMethodTransformer transformer;
    private final CodeGeneratorService codeGeneratorService;

    public PaymentMethodModel create(PaymentMethodModel model) {

        UUID organizationId = model.getOrganizationModel().getId();

        if (model.getCode() == null || model.getCode().isBlank()) {
            model.setCode(
                    codeGeneratorService.generatePaymentMethodCode(
                            organizationId));
        }

        if (service.existsByCodeIgnoreCaseAndOrganization_Id(model.getCode(), organizationId)) {
            throw new RuntimeException(
                    "Payment Method Code already exists : "
                            + model.getCode());
        }

        if (service.existsByMethodNameIgnoreCaseAndOrganization_Id(model.getMethodName(), organizationId)) {
            throw new RuntimeException(
                    "Payment Method already exists : "
                            + model.getMethodName());
        }

        if (model.getOnline() == null)
            model.setOnline(false);

        if (model.getCashBased() == null)
            model.setCashBased(false);

        if (model.getIsActive() == null)
            model.setIsActive(true);

        PaymentMethod saved = service.create(transformer.toEntity(model));

        return transformer.toModel(saved);
    }

    public PageResponse<PaymentMethodModel> getAll(PaymentMethodSearchCriteria criteria, Pageable pageable) {

        Page<PaymentMethod> page = service.search(criteria, pageable);

        return PageResponse.<PaymentMethodModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public PaymentMethodModel update(UUID id, PaymentMethodModel model) {
        PaymentMethod updated =
                service.update(
                        id,
                        transformer.toEntity(model));

        return transformer.toModel(updated);
    }

    public PaymentMethodModel delete(UUID id) {

        return transformer.toModel(service.delete(id));
    }

    public PaymentMethodModel restore(UUID id) {
        return transformer.toModel(service.restore(id));
    }
}