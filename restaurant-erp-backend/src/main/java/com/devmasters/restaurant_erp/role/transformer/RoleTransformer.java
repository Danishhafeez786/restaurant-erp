package com.devmasters.restaurant_erp.role.transformer;

import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.role.domain.Role;
import com.devmasters.restaurant_erp.role.model.RoleModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RoleTransformer extends Transformer<Role, RoleModel> {
    private final OrganizationTransformer organizationTransformer;

    @Override
    public Role toEntity(RoleModel model) {
        if(model == null)
            return null;
        return Role.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .roleName(model.getRoleName())
                .description(model.getDescription())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public RoleModel toModel(Role entity) {
        if(entity == null)
            return null;
        return RoleModel.builder()
                .id(entity.getId())
                .roleName(entity.getRoleName())
                .description(entity.getDescription())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
