package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Vendor;
import com.devmasters.restaurant_erp.model.VendorModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.VendorSearchCriteria;
import com.devmasters.restaurant_erp.service.VendorService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.VendorTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class VendorHandler {

    private final VendorService service;
    private final VendorTransformer transformer;
    private final CodeGeneratorService codeGeneratorService;

    public VendorModel create(VendorModel model) {

        UUID organizationId = model.getOrganizationModel().getId();

        if (model.getVendorCode() == null || model.getVendorCode().isBlank()) {

            model.setVendorCode(codeGeneratorService.generateExpenseVendorCode(organizationId));
        }

        if (service.existsByVendorCodeIgnoreCaseAndOrganization_Id(model.getVendorCode(), organizationId)) {
            throw new RuntimeException(
                    "Vendor Code already exists : "
                            + model.getVendorCode());
        }

        if (service.existsByVendorNameIgnoreCaseAndOrganization_Id(model.getVendorName(), organizationId)) {
            throw new RuntimeException(
                    "Vendor already exists : "
                            + model.getVendorName());
        }

        if (model.getEmail() != null &&
                !model.getEmail().isBlank() &&
                service.existsByEmailIgnoreCaseAndOrganization_Id(
                        model.getEmail(),
                        organizationId)) {
            throw new RuntimeException(
                    "Email already exists : "
                            + model.getEmail());
        }

        if (model.getIsActive() == null) model.setIsActive(true);
        Vendor saved = service.create(transformer.toEntity(model));
        return transformer.toModel(saved);
    }

    public PageResponse<VendorModel> getAll(VendorSearchCriteria criteria, Pageable pageable) {

        Page<Vendor> page = service.search(criteria, pageable);
        return PageResponse.<VendorModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public VendorModel update(UUID id, VendorModel model) {
        Vendor updated =
                service.update(
                        id,
                        transformer.toEntity(model));
        return transformer.toModel(updated);
    }

    public VendorModel delete(UUID id) {
        return transformer.toModel(service.delete(id));
    }

    public VendorModel restore(UUID id) {
        return transformer.toModel(service.restore(id));
    }
}