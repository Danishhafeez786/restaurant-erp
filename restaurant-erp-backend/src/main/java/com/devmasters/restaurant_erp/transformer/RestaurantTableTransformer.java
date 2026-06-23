package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.RestaurantTable;
import com.devmasters.restaurant_erp.model.RestaurantTableModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RestaurantTableTransformer extends Transformer<RestaurantTable, RestaurantTableModel>{
    private final BranchTransformer branchTransformer;

    @Override
    public RestaurantTable toEntity(RestaurantTableModel model) {
        if(model == null)
            return null;
        return RestaurantTable.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .tableNumber(model.getTableNumber())
                .capacity(model.getCapacity())
                .qrToken(model.getQrToken())
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
                .capacity(entity.getCapacity())
                .qrToken(entity.getQrToken())
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
