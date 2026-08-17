package com.devmasters.restaurant_erp.employee.respository.custom;

import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.employee.model.searchCriteria.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import org.springframework.data.domain.Pageable;

public interface EmployeeRepositoryCustomRepository {

    PageResponse<Employee> search(EmployeeSearchCriteria criteria, Pageable pageable);

}
