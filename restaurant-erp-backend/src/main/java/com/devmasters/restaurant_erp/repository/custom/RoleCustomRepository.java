package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Role;
import com.devmasters.restaurant_erp.model.searchcriteria.RoleSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleCustomRepository {

    Page<Role> search(RoleSearchCriteria criteria, Pageable pageable);
}
