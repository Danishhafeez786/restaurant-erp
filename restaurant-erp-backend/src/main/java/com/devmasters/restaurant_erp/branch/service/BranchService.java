package com.devmasters.restaurant_erp.branch.service;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.branch.model.searchCriteria.BranchSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BranchService {

    boolean existsByBranchNameIgnoreCase(String branchName);

    boolean existsByBranchCodeIgnoreCase(String branchCode);

    Branch create(Branch entity);

    Page<Branch> search(BranchSearchCriteria criteria,Pageable pageable);

    Branch findById(UUID id);

    Branch update(UUID id, Branch entity);

    Branch delete(UUID id);

    Branch restore(UUID id);

    String createBranchCode(OrganizationModel organizationModel);
}