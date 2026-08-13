package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Tax;
import com.devmasters.restaurant_erp.model.TaxModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class TaxTransformer extends Transformer<Tax, TaxModel> {

    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public Tax toEntity(TaxModel model) {
        if (model == null)
            return null;

        return Tax.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .taxCode(model.getTaxCode())
                .taxName(model.getTaxName())
                .calculationType(model.getCalculationType())
                .rate(model.getRate())
                .description(model.getDescription())
                .defaultTax(model.getDefaultTax())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(true)
                .build();
    }

    @Override
    public TaxModel toModel(Tax entity) {
        if (entity == null)
            return null;

        return TaxModel.builder()
                .id(entity.getId())
                .taxCode(entity.getTaxCode())
                .taxName(entity.getTaxName())
                .calculationType(entity.getCalculationType())
                .rate(entity.getRate())
                .description(entity.getDescription())
                .defaultTax(entity.getDefaultTax())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .build();
    }
}