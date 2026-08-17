package com.devmasters.restaurant_erp.permission.handler;

import com.devmasters.restaurant_erp.permission.domain.Permission;
import com.devmasters.restaurant_erp.permission.model.PermissionModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.permission.model.searchCriteria.PermissionSearchCriteria;
import com.devmasters.restaurant_erp.permission.service.PermissionService;
import com.devmasters.restaurant_erp.permission.transformer.PermissionTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PermissionHandler {

    private final PermissionService permissionService;
    private final PermissionTransformer permissionTransformer;

    public List<PermissionModel> create(PermissionModel model) {

        if (permissionService.existsByModuleIgnoreCase(model.getModule())) {
            throw new RuntimeException(
                    "Permissions already exist for module : " + model.getModule()
            );
        }

        List<Permission> permissions =
                permissionService.create(model.getModule());

        return permissionTransformer.toModels(permissions);
    }

    public PageResponse<PermissionModel> getAll(
            PermissionSearchCriteria criteria,
            Pageable pageable) {

        Page<Permission> page =
                permissionService.search(criteria, pageable);

        return PageResponse.<PermissionModel>builder()
                .content(
                        permissionTransformer.toModels(page.getContent())
                )
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public PermissionModel update(
            UUID id,
            PermissionModel model) {

        Permission entity =
                permissionTransformer.toEntity(model);

        Permission updated =
                permissionService.update(id, entity);

        return permissionTransformer.toModel(updated);
    }

    public PermissionModel delete(UUID id) {

        Permission deleted =
                permissionService.delete(id);

        return permissionTransformer.toModel(deleted);
    }

    public PermissionModel restore(UUID id) {

        Permission restored =
                permissionService.restore(id);

        return permissionTransformer.toModel(restored);
    }
}