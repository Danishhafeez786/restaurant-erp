package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.model.BranchModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BranchTransformer extends Transformer<Branch, BranchModel>{
    private final OrganizationTransformer organizationTransformer;

    @Override
    public Branch toEntity(BranchModel model) {
        if(model == null)
            return null;
        return Branch.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .branchName(model.getBranchName())
                .branchCode(model.getBranchCode())
                .address(model.getAddress())
                .city(model.getCity())
                .phone(model.getPhone())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public BranchModel toModel(Branch entity) {
        if(entity == null)
            return null;
        return BranchModel.builder()
                .id(entity.getId())
                .branchName(entity.getBranchName())
                .branchCode(entity.getBranchCode())
                .address(entity.getAddress())
                .city(entity.getCity())
                .phone(entity.getPhone())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
