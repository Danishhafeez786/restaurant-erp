package com.devmasters.restaurant_erp.permission.transformer;

import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.permission.domain.Permission;
import com.devmasters.restaurant_erp.permission.model.PermissionModel;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PermissionTransformer extends Transformer<Permission, PermissionModel> {
    @Override
    public Permission toEntity(PermissionModel model) {
        if(model == null)
            return null;
        return Permission.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .code(model.getCode())
                .name(model.getName())
                .module(model.getModule())
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public PermissionModel toModel(Permission entity) {
        if(entity == null)
            return null;
        return PermissionModel.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .module(entity.getModule())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
