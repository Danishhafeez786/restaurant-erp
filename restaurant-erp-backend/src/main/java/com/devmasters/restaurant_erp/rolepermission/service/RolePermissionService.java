package com.devmasters.restaurant_erp.rolepermission.service;

import com.devmasters.restaurant_erp.permission.domain.Permission;
import com.devmasters.restaurant_erp.role.domain.Role;
import com.devmasters.restaurant_erp.rolepermission.domain.RolePermission;
import com.devmasters.restaurant_erp.rolepermission.model.searchCriteria.RolePermissionSearchCriteria;
import com.devmasters.restaurant_erp.rolepermission.model.settings.PermissionMatrixResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RolePermissionService {

    boolean existsByRoleAndPermission(Role role, Permission permission);

    RolePermission create(RolePermission entity);

    Page<RolePermission> search(RolePermissionSearchCriteria criteria, Pageable pageable);

    RolePermission findById(UUID id);

    RolePermission update(UUID id,
                          RolePermission entity);

    RolePermission delete(UUID id);

    RolePermission restore(UUID id);

    PermissionMatrixResponse getPermissionMatrix();
}
