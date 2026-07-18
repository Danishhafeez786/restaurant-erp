package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.RolePermission;
import com.devmasters.restaurant_erp.model.searchcriteria.RolePermissionSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RolePermissionSearchRepository {
    Page<RolePermission> search(RolePermissionSearchCriteria criteria, Pageable pageable);
}
