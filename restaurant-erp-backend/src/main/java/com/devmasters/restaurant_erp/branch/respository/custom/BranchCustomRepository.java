package com.devmasters.restaurant_erp.branch.respository.custom;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.branch.model.searchCriteria.BranchSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BranchCustomRepository {

    Page<Branch> search(
            BranchSearchCriteria criteria,
            Pageable pageable
    );
}
