package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Permission;
import com.devmasters.restaurant_erp.model.searchcriteria.PermissionSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    boolean existsByModuleIgnoreCase(String module);

    List<Permission> create(String module);

    Page<Permission> search(
            PermissionSearchCriteria criteria,
            Pageable pageable
    );

    Permission findById(UUID id);

    Permission update(UUID id, Permission entity);

    Permission delete(UUID id);

    Permission restore(UUID id);
}