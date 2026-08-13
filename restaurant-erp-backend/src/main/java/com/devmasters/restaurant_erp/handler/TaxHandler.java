package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Tax;
import com.devmasters.restaurant_erp.model.TaxModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.TaxSearchCriteria;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.service.TaxService;
import com.devmasters.restaurant_erp.transformer.TaxTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaxHandler {

    private final TaxService taxService;
    private final TaxTransformer taxTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public TaxModel create(TaxModel model) {
        UUID organizationId = model.getOrganizationModel().getId();

        if (taxService.existsByTaxNameIgnoreCase(model.getTaxName(), organizationId))
            throw new RuntimeException("Tax already exists with name : " + model.getTaxName());

        if (taxService.existsByTaxCodeIgnoreCase(model.getTaxCode(), organizationId))
            throw new RuntimeException("Tax already exists with code : " + model.getTaxCode());

        if (Boolean.TRUE.equals(model.getDefaultTax())
                && taxService.existsByDefaultTax(organizationId))
            throw new RuntimeException("Default tax already exists for this organization.");

        if (model.getTaxCode() == null || model.getTaxCode().isBlank())
            model.setTaxCode(codeGeneratorService.generateTaxCode(organizationId));

        Tax entity = taxTransformer.toEntity(model);
        Tax saved = taxService.create(entity);

        return taxTransformer.toModel(saved);
    }

    public PageResponse<TaxModel> getAll(TaxSearchCriteria criteria, Pageable pageable) {
        Page<Tax> page = taxService.search(criteria, pageable);

        return PageResponse.<TaxModel>builder()
                .content(taxTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public TaxModel getById(UUID id) {
        return taxTransformer.toModel(taxService.findById(id));
    }

    public TaxModel update(UUID id, TaxModel model) {
        Tax existing = taxService.findById(id);
        UUID organizationId = existing.getOrganization().getId();

        if (taxService.existsByTaxNameIgnoreCaseAndIdNot(
                model.getTaxName(), organizationId, id))
            throw new RuntimeException("Tax already exists with name : " + model.getTaxName());

        if (taxService.existsByTaxCodeIgnoreCaseAndIdNot(
                model.getTaxCode(), organizationId, id))
            throw new RuntimeException("Tax already exists with code : " + model.getTaxCode());

        if (Boolean.TRUE.equals(model.getDefaultTax())
                && taxService.existsByDefaultTaxAndIdNot(organizationId, id))
            throw new RuntimeException("Default tax already exists for this organization.");

        Tax entity = taxTransformer.toEntity(model);
        Tax updated = taxService.update(id, entity);

        return taxTransformer.toModel(updated);
    }

    public TaxModel delete(UUID id) {
        Tax tax = taxService.findById(id);

        if (!Boolean.TRUE.equals(tax.getIsActive()))
            throw new RuntimeException("Tax already deleted.");

        return taxTransformer.toModel(taxService.delete(id));
    }

    public TaxModel restore(UUID id) {
        Tax tax = taxService.findById(id);

        if (Boolean.TRUE.equals(tax.getIsActive()))
            throw new RuntimeException("Tax is already active.");

        return taxTransformer.toModel(taxService.restore(id));
    }
}