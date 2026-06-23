package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository  extends MongoRepository<Role, UUID> {
}
