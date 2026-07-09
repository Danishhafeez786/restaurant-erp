package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.repository.custom.BranchCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchRepository extends MongoRepository<Branch, UUID>,
        BranchCustomRepository {

    boolean existsByBranchNameIgnoreCase(String branchName);

    boolean existsByBranchCodeIgnoreCase(String branchCode);

    long countByOrganization(Organization organization);
}
