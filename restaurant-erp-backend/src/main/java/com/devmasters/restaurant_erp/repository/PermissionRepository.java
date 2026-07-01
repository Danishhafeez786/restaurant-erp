package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Permission;
import com.devmasters.restaurant_erp.repository.custom.PermissionCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, UUID>,
        PermissionCustomRepository {

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByNameIgnoreCase(String name);
    boolean existsByModuleIgnoreCase(String module);
    List<Permission> findAllByIsActiveTrue();
}
