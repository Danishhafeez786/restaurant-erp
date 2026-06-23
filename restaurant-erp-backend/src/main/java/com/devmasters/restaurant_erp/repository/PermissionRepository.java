package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PermissionRepository  extends MongoRepository<Permission, UUID>{
}
