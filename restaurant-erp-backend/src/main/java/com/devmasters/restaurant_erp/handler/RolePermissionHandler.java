package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.RolePermission;
import com.devmasters.restaurant_erp.model.RolePermissionModel;
import com.devmasters.restaurant_erp.model.searchcriteria.RolePermissionSearchCriteria;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.settings.PermissionMatrixResponse;
import com.devmasters.restaurant_erp.service.RolePermissionService;
import com.devmasters.restaurant_erp.transformer.RolePermissionTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RolePermissionHandler {
    private final RolePermissionTransformer rolePermissionTransformer;
    private final RolePermissionService rolePermissionService;

    public RolePermissionModel create(RolePermissionModel model) {

        RolePermission entity = rolePermissionTransformer.toEntity(model);

        if (rolePermissionService.existsByRoleAndPermission(entity.getRole(), entity.getPermission()))
            throw new RuntimeException("Permission already assigned to this Role.");

        RolePermission saved = rolePermissionService.create(entity);
        return rolePermissionTransformer.toModel(saved);
    }

    public PageResponse<RolePermissionModel> getAll(RolePermissionSearchCriteria criteria,
                                                    Pageable pageable) {
        Page<RolePermission> page = rolePermissionService.search(criteria, pageable);
        return PageResponse.<RolePermissionModel>builder()
                .content(rolePermissionTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public RolePermissionModel update(UUID id, RolePermissionModel model) {
        RolePermission entity = rolePermissionTransformer.toEntity(model);
        RolePermission existing = rolePermissionService.findById(id);
        boolean changed =
                !existing.getRole().getId().equals(entity.getRole().getId()) ||
                        !existing.getPermission().getId().equals(entity.getPermission().getId());

        if (changed && rolePermissionService.existsByRoleAndPermission(entity.getRole(),
                entity.getPermission()))
            throw new RuntimeException("Permission already assigned to this Role.");


        RolePermission updated = rolePermissionService.update(id, entity);
        return rolePermissionTransformer.toModel(updated);
    }

    public RolePermissionModel delete(UUID id) {
        RolePermission deleted = rolePermissionService.delete(id);
        return rolePermissionTransformer.toModel(deleted);
    }

    public RolePermissionModel restore(UUID id) {
        RolePermission restored = rolePermissionService.restore(id);
        return rolePermissionTransformer.toModel(restored);
    }

    public PermissionMatrixResponse getMatrix() {
        return rolePermissionService.getPermissionMatrix();
    }
}
