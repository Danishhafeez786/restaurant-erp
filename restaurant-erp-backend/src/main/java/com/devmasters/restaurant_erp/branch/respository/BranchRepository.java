package com.devmasters.restaurant_erp.branch.respository;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.branch.respository.custom.BranchCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BranchRepository extends MongoRepository<Branch, UUID>, BranchCustomRepository {

    boolean existsByBranchNameIgnoreCase(String branchName);

    boolean existsByBranchCodeIgnoreCase(String branchCode);

    long countByOrganization(Organization organization);
}
