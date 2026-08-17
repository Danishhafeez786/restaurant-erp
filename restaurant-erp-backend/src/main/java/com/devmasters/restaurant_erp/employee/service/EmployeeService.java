package com.devmasters.restaurant_erp.employee.service;

import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.employee.model.EmployeeModel;
import com.devmasters.restaurant_erp.employee.model.searchCriteria.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeeService {

    Employee create(Employee employee);

    /**
     * Update Employee + User
     */
    Employee update(UUID id, EmployeeModel model, Employee employee);

    Employee delete(Employee employee);

    Employee restore(Employee employee);

    PageResponse<Employee> search(EmployeeSearchCriteria criteria, Pageable pageable);

    long count();

    Employee findById(UUID id);
}