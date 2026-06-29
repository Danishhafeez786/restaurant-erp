package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Role;
import com.devmasters.restaurant_erp.model.RoleModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.RoleSearchCriteria;
import com.devmasters.restaurant_erp.service.RoleService;
import com.devmasters.restaurant_erp.transformer.RoleTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RoleHandler {

    private final RoleService roleService;
    private final RoleTransformer roleTransformer;

    public RoleModel create(RoleModel model) {

        if (roleService.existsByRoleNameIgnoreCase(model.getRoleName())) {
            throw new RuntimeException(
                    "Role already exists with name : " + model.getRoleName()
            );
        }

        Role entity = roleTransformer.toEntity(model);

        Role saved = roleService.create(entity);

        return roleTransformer.toModel(saved);
    }

    public PageResponse<RoleModel> getAll(
            RoleSearchCriteria criteria,
            Pageable pageable) {

        Page<Role> page =
                roleService.search(criteria, pageable);

        return PageResponse.<RoleModel>builder()
                .content(
                        roleTransformer.toModels(page.getContent())
                )
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public RoleModel update(
            UUID id,
            RoleModel model) {

        Role entity =
                roleTransformer.toEntity(model);

        Role updated =
                roleService.update(id, entity);

        return roleTransformer.toModel(updated);
    }

    public RoleModel delete(UUID id) {

        Role deleted =
                roleService.delete(id);

        return roleTransformer.toModel(deleted);
    }

    public RoleModel restore(UUID id) {

        Role restored =
                roleService.restore(id);

        return roleTransformer.toModel(restored);
    }
}