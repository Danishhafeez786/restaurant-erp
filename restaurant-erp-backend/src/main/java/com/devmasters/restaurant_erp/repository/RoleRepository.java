package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Role;
import com.devmasters.restaurant_erp.repository.custom.RoleCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends MongoRepository<Role, UUID>, RoleCustomRepository {

    boolean existsByRoleNameIgnoreCase(String roleName);
    List<Role> findAllByIsActiveTrue();
}
