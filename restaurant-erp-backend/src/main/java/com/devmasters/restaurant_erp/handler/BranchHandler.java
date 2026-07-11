package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.BranchSearchCriteria;
import com.devmasters.restaurant_erp.service.BranchService;
import com.devmasters.restaurant_erp.transformer.BranchTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BranchHandler {

    private final BranchService branchService;
    private final BranchTransformer branchTransformer;

    public BranchModel create(BranchModel model) {
        if (branchService.existsByBranchNameIgnoreCase(model.getBranchName())) {
            throw new RuntimeException("Branch already exists with name : " + model.getBranchName());
        }

        if (branchService.existsByBranchCodeIgnoreCase(model.getBranchCode())) {
            throw new RuntimeException("Branch already exists with code : " + model.getBranchCode());
        }
        model.setBranchCode(model.getOrganizationModel()
                .getOrganizationName() +  " - Branch - " + branchService
                .createBranchCode(model.getOrganizationModel()));

        Branch entity = branchTransformer.toEntity(model);
        Branch saved = branchService.create(entity);
        return branchTransformer.toModel(saved);
    }

    public PageResponse<BranchModel> getAll(BranchSearchCriteria criteria, Pageable pageable) {
        Page<Branch> page = branchService.search(criteria, pageable);
        return PageResponse.<BranchModel>builder()
                .content(branchTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public BranchModel update(UUID id, BranchModel model) {
        Branch entity = branchTransformer.toEntity(model);
        Branch updated = branchService.update(id, entity);
        return branchTransformer.toModel(updated);
    }

    public BranchModel delete(UUID id) {
        Branch deleted = branchService.delete(id);
        return branchTransformer.toModel(deleted);
    }

    public BranchModel restore(UUID id) {
        Branch restored = branchService.restore(id);
        return branchTransformer.toModel(restored);
    }
}