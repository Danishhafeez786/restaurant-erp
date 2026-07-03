package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.Permission;
import com.devmasters.restaurant_erp.enums.PermissionAction;
import com.devmasters.restaurant_erp.model.searchcriteria.PermissionSearchCriteria;
import com.devmasters.restaurant_erp.repository.PermissionRepository;
import com.devmasters.restaurant_erp.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public boolean existsByModuleIgnoreCase(String module) {
        return permissionRepository.existsByModuleIgnoreCase(module);
    }

    @Override
    public List<Permission> create(String module) {

        String moduleCode = module.trim().toUpperCase();
        String moduleName = toSentenceCase(module);

        List<Permission> permissions = Arrays.stream(PermissionAction.values())
                .map(action -> Permission.builder()
                        .id(UUID.randomUUID())
                        .module(moduleCode)
                        .code(moduleCode + "_" + action.name())
                        .name(moduleName + " " + action.getDisplayName())
                        .isActive(true)
                        .build())
                .collect(Collectors.toList());

        return permissionRepository.saveAll(permissions);
    }

    @Override
    public Page<Permission> search(
            PermissionSearchCriteria criteria,
            Pageable pageable) {

        return permissionRepository.search(criteria, pageable);
    }

    @Override
    public Permission findById(UUID id) {

        return permissionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Permission not found."));
    }

    @Override
    public Permission update(
            UUID id,
            Permission entity) {

        Permission existing = findById(id);

        existing.setCode(entity.getCode());
        existing.setName(entity.getName());
        existing.setModule(entity.getModule());
        existing.setIsActive(entity.getIsActive());

        return permissionRepository.save(existing);
    }

    @Override
    public Permission delete(UUID id) {

        Permission permission = findById(id);

        if (!Boolean.TRUE.equals(permission.getIsActive())) {
            throw new RuntimeException("Permission already deleted.");
        }

        permission.setIsActive(false);

        return permissionRepository.save(permission);
    }

    @Override
    public Permission restore(UUID id) {

        Permission permission = findById(id);

        permission.setIsActive(true);

        return permissionRepository.save(permission);
    }

    private String toSentenceCase(String text) {

        if (text == null || text.isBlank())
            return text;
        text = text.trim();
        return Character.toUpperCase(text.charAt(0)) + text.substring(1).toLowerCase();
    }
}