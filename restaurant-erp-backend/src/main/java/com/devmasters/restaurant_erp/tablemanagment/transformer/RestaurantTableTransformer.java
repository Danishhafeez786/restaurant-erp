package com.devmasters.restaurant_erp.tablemanagment.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.tablemanagment.domain.RestaurantTable;
import com.devmasters.restaurant_erp.tablemanagment.model.RestaurantTableModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RestaurantTableTransformer extends Transformer<RestaurantTable, RestaurantTableModel> {
    private final BranchTransformer branchTransformer;
    private final OrganizationTransformer organizationTransformer;

    @Override
    public RestaurantTable toEntity(RestaurantTableModel model) {
        if(model == null)
            return null;
        return RestaurantTable.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .tableNumber(model.getTableNumber())
                .tableName(model.getTableName())
                .capacity(model.getCapacity())
                .qrCode(model.getQrToken())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public RestaurantTableModel toModel(RestaurantTable entity) {
        if(entity == null)
            return null;
        return RestaurantTableModel.builder()
                .id(entity.getId())
                .tableNumber(entity.getTableNumber())
                .tableName(entity.getTableName())
                .capacity(entity.getCapacity())
                .qrToken(entity.getQrCode())
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
