package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Role;
import com.devmasters.restaurant_erp.model.searchcriteria.RoleSearchCriteria;
import com.devmasters.restaurant_erp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public boolean existsByRoleNameIgnoreCase(String roleName) {
        return roleRepository.existsByRoleNameIgnoreCase(roleName);
    }

    @Override
    public Role create(Role entity) {
        return roleRepository.save(entity);
    }

    @Override
    public Page<Role> search(
            RoleSearchCriteria criteria,
            Pageable pageable) {

        return roleRepository.search(criteria, pageable);
    }

    @Override
    public Role findById(UUID id) {

        return roleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Role not found."));
    }

    @Override
    public Role update(
            UUID id,
            Role entity) {

        Role existing = findById(id);

        existing.setRoleName(entity.getRoleName());
        existing.setDescription(entity.getDescription());
        existing.setOrganization(entity.getOrganization());
        existing.setIsActive(entity.getIsActive());

        return roleRepository.save(existing);
    }

    @Override
    public Role delete(UUID id) {

        Role role = findById(id);

        if (!Boolean.TRUE.equals(role.getIsActive())) {
            throw new RuntimeException("Role already deleted.");
        }

        role.setIsActive(false);

        return roleRepository.save(role);
    }

    @Override
    public Role restore(UUID id) {

        Role role = findById(id);

        role.setIsActive(true);

        return roleRepository.save(role);
    }
}