package com.devmasters.restaurant_erp.rolepermission.respository;

import com.devmasters.restaurant_erp.permission.domain.Permission;
import com.devmasters.restaurant_erp.role.domain.Role;
import com.devmasters.restaurant_erp.rolepermission.domain.RolePermission;
import com.devmasters.restaurant_erp.rolepermission.respository.custom.RolePermissionSearchRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends MongoRepository<RolePermission, UUID>,
        RolePermissionSearchRepository {

    boolean existsByRoleAndPermission(Role role, Permission permission);

    List<RolePermission> findByRole(Role role);

    List<RolePermission> findByRoleAndIsActiveTrue(Role role);

    List<RolePermission> findByPermission(Permission permission);

    List<RolePermission> findAll();

}