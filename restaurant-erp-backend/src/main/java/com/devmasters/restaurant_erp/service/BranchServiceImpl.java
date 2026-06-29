package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.model.searchcriteria.BranchSearchCriteria;
import com.devmasters.restaurant_erp.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    public boolean existsByBranchNameIgnoreCase(String branchName) {
        return branchRepository.existsByBranchNameIgnoreCase(branchName);
    }

    @Override
    public boolean existsByBranchCodeIgnoreCase(String branchCode) {
        return branchRepository.existsByBranchCodeIgnoreCase(branchCode);
    }

    @Override
    public Branch create(Branch entity) {
        return branchRepository.save(entity);
    }

    @Override
    public Page<Branch> search(
            BranchSearchCriteria criteria,
            Pageable pageable) {

        return branchRepository.search(criteria, pageable);
    }

    @Override
    public Branch findById(UUID id) {

        return branchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Branch not found."));
    }

    @Override
    public Branch update(
            UUID id,
            Branch entity) {

        Branch existing = findById(id);

        existing.setBranchName(entity.getBranchName());
        existing.setBranchCode(entity.getBranchCode());
        existing.setAddress(entity.getAddress());
        existing.setCity(entity.getCity());
        existing.setPhone(entity.getPhone());
        existing.setOrganization(entity.getOrganization());
        existing.setIsActive(entity.getIsActive());

        return branchRepository.save(existing);
    }

    @Override
    public Branch delete(UUID id) {

        Branch branch = findById(id);

        if (!Boolean.TRUE.equals(branch.getIsActive())) {
            throw new RuntimeException("Branch already deleted.");
        }

        branch.setIsActive(false);

        return branchRepository.save(branch);
    }

    @Override
    public Branch restore(UUID id) {

        Branch branch = findById(id);

        branch.setIsActive(true);

        return branchRepository.save(branch);
    }
}