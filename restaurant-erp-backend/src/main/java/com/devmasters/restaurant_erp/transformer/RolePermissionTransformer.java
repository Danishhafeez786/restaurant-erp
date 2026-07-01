package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.RolePermission;
import com.devmasters.restaurant_erp.model.RolePermissionModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RolePermissionTransformer extends Transformer<RolePermission, RolePermissionModel>{
    private final RoleTransformer roleTransformer;
    private final PermissionTransformer permissionTransformer;
    private final OrganizationTransformer organizationTransformer;

    @Override
    public RolePermission toEntity(RolePermissionModel model) {
        if(model == null)
            return null;
        return RolePermission.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .role(roleTransformer.toEntity(model.getRoleModel()))
                .permission(permissionTransformer.toEntity(model.getPermissionModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public RolePermissionModel toModel(RolePermission entity) {
        if(entity == null)
            return null;
        return RolePermissionModel.builder()
                .id(entity.getId())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .roleModel(roleTransformer.toModel(entity.getRole()))
                .permissionModel(permissionTransformer.toModel(entity.getPermission()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
