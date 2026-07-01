package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Permission;
import com.devmasters.restaurant_erp.domain.Role;
import com.devmasters.restaurant_erp.domain.RolePermission;
import com.devmasters.restaurant_erp.model.RolePermissionSearchCriteria;
import com.devmasters.restaurant_erp.model.settings.*;
import com.devmasters.restaurant_erp.repository.PermissionRepository;
import com.devmasters.restaurant_erp.repository.RolePermissionRepository;
import com.devmasters.restaurant_erp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService{
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public boolean existsByRoleAndPermission(Role role, Permission permission) {
        return rolePermissionRepository.existsByRoleAndPermission(role, permission);
    }

    @Override
    public RolePermission create(RolePermission entity) {
        return rolePermissionRepository.save(entity);
    }

    @Override
    public Page<RolePermission> search(RolePermissionSearchCriteria criteria, Pageable pageable) {
        return rolePermissionRepository.search(criteria, pageable);
    }

    @Override
    public RolePermission findById(UUID id) {
        return rolePermissionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Role Permission not found."));
    }

    @Override
    public RolePermission update(UUID id, RolePermission entity) {
        RolePermission existing = findById(id);
        existing.setOrganization(entity.getOrganization());
        existing.setRole(entity.getRole());
        existing.setPermission(entity.getPermission());
        existing.setIsActive(entity.getIsActive());
        return rolePermissionRepository.save(existing);
    }

    @Override
    public RolePermission delete(UUID id) {
        RolePermission entity = findById(id);
        if (!Boolean.TRUE.equals(entity.getIsActive()))
            throw new RuntimeException(
                    "Role Permission already deleted."
            );
        entity.setIsActive(false);
        return rolePermissionRepository.save(entity);
    }

    @Override
    public RolePermission restore(UUID id) {
        RolePermission entity = findById(id);
        entity.setIsActive(true);
        return rolePermissionRepository.save(entity);
    }

    @Override
    public PermissionMatrixResponse getPermissionMatrix() {

        List<Role> roles = roleRepository.findAllByIsActiveTrue();
        List<Permission> permissions = permissionRepository.findAllByIsActiveTrue();
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();

        List<RoleMatrixModel> roleModels = getRoleMatrixModels(roles);

        List<ModulePermissionModel> moduleModels = getModulePermissionModels(permissions);

        Map<String, RolePermission> assignmentMap = getStringRolePermissionMap(rolePermissions);

        List<RolePermissionAssignmentModel> assignments = getRolePermissionAssignmentModels(roles, permissions, assignmentMap);

        return PermissionMatrixResponse.builder()
                .roles(roleModels)
                .modules(moduleModels)
                .assignments(assignments)
                .build();

    }

    private static @NonNull List<RoleMatrixModel> getRoleMatrixModels(List<Role> roles) {
        List<RoleMatrixModel> roleModels = roles.stream()
                .map(role -> RoleMatrixModel.builder()
                        .id(role.getId())
                        .roleName(role.getRoleName())
                        .isActive(role.getIsActive())
                        .build())
                .toList();
        return roleModels;
    }

    private static @NonNull List<ModulePermissionModel> getModulePermissionModels(List<Permission> permissions) {
        Map<String, List<Permission>> groupedPermissions =
                permissions.stream()
                        .collect(Collectors.groupingBy(Permission::getModule));

        List<ModulePermissionModel> moduleModels = new ArrayList<>();

        groupedPermissions.forEach((module, permissionList) -> {

            List<PermissionMatrixModel> permissionModels =
                    permissionList.stream()
                            .map(permission -> PermissionMatrixModel.builder()
                                    .id(permission.getId())
                                    .name(permission.getName())
                                    .code(permission.getCode())
                                    .isActive(permission.getIsActive())
                                    .build())
                            .toList();

            moduleModels.add(
                    ModulePermissionModel.builder()
                            .module(module)
                            .permissions(permissionModels)
                            .build()
            );

        });
        return moduleModels;
    }

    private static @NonNull Map<String, RolePermission> getStringRolePermissionMap(List<RolePermission> rolePermissions) {
        Map<String, RolePermission> assignmentMap =
                rolePermissions.stream()
                        .collect(Collectors.toMap(
                                rp -> rp.getRole().getId() + "_" + rp.getPermission().getId(),
                                Function.identity(),
                                (existing, replacement) -> existing
                        ));
        return assignmentMap;
    }

    private static @NonNull List<RolePermissionAssignmentModel> getRolePermissionAssignmentModels(List<Role> roles, List<Permission> permissions, Map<String, RolePermission> assignmentMap) {
        List<RolePermissionAssignmentModel> assignments = new ArrayList<>();

        for (Role role : roles) {

            for (Permission permission : permissions) {

                String key = role.getId() + "_" + permission.getId();

                RolePermission rolePermission = assignmentMap.get(key);

                if (rolePermission != null) {

                    assignments.add(
                            RolePermissionAssignmentModel.builder()
                                    .rolePermissionId(rolePermission.getId())
                                    .roleId(role.getId())
                                    .permissionId(permission.getId())
                                    .assigned(true)
                                    .isActive(rolePermission.getIsActive())
                                    .build()
                    );

                } else {

                    assignments.add(
                            RolePermissionAssignmentModel.builder()
                                    .rolePermissionId(null)
                                    .roleId(role.getId())
                                    .permissionId(permission.getId())
                                    .assigned(false)
                                    .isActive(false)
                                    .build()
                    );

                }

            }

        }
        return assignments;
    }
}
