package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Permission;
import com.devmasters.restaurant_erp.model.searchcriteria.PermissionSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionCustomRepository {

    Page<Permission> search(
            PermissionSearchCriteria criteria,
            Pageable pageable
    );
}