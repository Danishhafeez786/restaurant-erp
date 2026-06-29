package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.model.searchcriteria.BranchSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BranchCustomRepository {

    Page<Branch> search(
            BranchSearchCriteria criteria,
            Pageable pageable
    );
}
