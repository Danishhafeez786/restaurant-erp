package com.devmasters.restaurant_erp.role.respository.custom;

import com.devmasters.restaurant_erp.role.domain.Role;
import com.devmasters.restaurant_erp.role.model.searchCriteria.RoleSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleCustomRepository {

    Page<Role> search(RoleSearchCriteria criteria, Pageable pageable);
}
