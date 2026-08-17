package com.devmasters.restaurant_erp.role.service;

import com.devmasters.restaurant_erp.role.domain.Role;
import com.devmasters.restaurant_erp.role.model.searchCriteria.RoleSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RoleService {

    boolean existsByRoleNameIgnoreCase(String roleName);

    Role create(Role entity);

    Page<Role> search(RoleSearchCriteria criteria, Pageable pageable);

    Role findById(UUID id);

    Role update(UUID id, Role entity);

    Role delete(UUID id);

    Role restore(UUID id);
}