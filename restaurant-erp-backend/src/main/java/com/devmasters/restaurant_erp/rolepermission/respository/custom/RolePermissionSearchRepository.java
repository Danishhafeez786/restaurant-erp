package com.devmasters.restaurant_erp.rolepermission.respository.custom;

import com.devmasters.restaurant_erp.rolepermission.domain.RolePermission;
import com.devmasters.restaurant_erp.rolepermission.model.searchCriteria.RolePermissionSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RolePermissionSearchRepository {
    Page<RolePermission> search(RolePermissionSearchCriteria criteria, Pageable pageable);
}
