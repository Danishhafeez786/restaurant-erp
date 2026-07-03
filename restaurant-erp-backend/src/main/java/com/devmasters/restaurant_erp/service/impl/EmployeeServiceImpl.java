package com.devmasters.restaurant_erp.service.impl;

import com.devmasters.restaurant_erp.domain.*;
import com.devmasters.restaurant_erp.model.empoyee.EmployeeModel;
import com.devmasters.restaurant_erp.model.empoyee.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.repository.EmployeeRepository;
import com.devmasters.restaurant_erp.repository.UserRepository;
import com.devmasters.restaurant_erp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final OrganizationService organizationService;
    private final BranchService branchService;
    private final RoleService roleService;
    private final UserRepository userRepository;

    @Override
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(UUID id, EmployeeModel model, Employee employee) {
        Organization organization = organizationService.findById(model.getOrganizationModel().getId());
        Branch branch = branchService.findById(model.getBranchModel().getId());
        Role role = roleService.findById(model.getRoleModel().getId());
        User user = employee.getUser();

        updateUserInfoAndSave(model, user, organization, role, branch);
        updateEmployee(model, employee, role, organization, branch, user);

        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public Employee delete(Employee employee) {
        User user = employee.getUser();
        user.setIsActive(false);
        userRepository.save(user);

        employee.setUser(user);
        employee.setIsActive(false);
        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public Employee restore(Employee employee) {
        User user = employee.getUser();
        user.setIsActive(true);
        userRepository.save(user);

        employee.setUser(user);
        employee.setIsActive(true);
        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public PageResponse<Employee> search(EmployeeSearchCriteria criteria, Pageable pageable) {
        return employeeRepository.search(criteria, pageable);
    }

    @Override
    public long count() {
        return employeeRepository.count();
    }

    @Override
    public Employee findById(UUID id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.orElse(null);
    }

    private static void updateEmployee(EmployeeModel model, Employee employee, Role role, Organization organization, Branch branch, User user) {
        employee.setPhone(model.getPhone());
        employee.setFullName(model.getFullName());
        employee.setCnic(model.getCnic());
        employee.setAddress(model.getAddress());
        employee.setEmergencyContact(model.getEmergencyContact());
        employee.setJoiningDate(model.getJoiningDate());
        employee.setSalary(model.getSalary());
        employee.setRole(role);
        employee.setOrganization(organization);
        employee.setBranch(branch);
        employee.setUser(user);
    }

    private void updateUserInfoAndSave(EmployeeModel model, User user, Organization organization, Role role, Branch branch) {
        user.setPhone(model.getPhone());
        user.setFullName(model.getFullName());
        user.setOrganization(organization);
        user.setRole(role);
        user.setBranch(branch);
        userRepository.save(user);
    }
}
