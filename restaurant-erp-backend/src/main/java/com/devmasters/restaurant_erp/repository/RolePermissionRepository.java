package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.RolePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RolePermissionRepository extends MongoRepository<RolePermission, UUID> {
}
