package com.devmasters.restaurant_erp.permission.respository.custom;

import com.devmasters.restaurant_erp.permission.domain.Permission;
import com.devmasters.restaurant_erp.permission.model.searchCriteria.PermissionSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionCustomRepository {

    Page<Permission> search(
            PermissionSearchCriteria criteria,
            Pageable pageable
    );
}