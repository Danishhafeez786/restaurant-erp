package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.*;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
import com.devmasters.restaurant_erp.model.employee.EmployeeRequestModel;
import com.devmasters.restaurant_erp.model.searchcriteria.EmployeeSearchCriteria;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.service.*;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.EmployeeTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmployeeHandler {

    private final EmployeeService employeeService;
    private final UserService userService;
    private final RoleService roleService;
    private final OrganizationService organizationService;
    private final BranchService branchService;
    private final EmployeeTransformer employeeTransformer;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorService codeGeneratorService;

    public EmployeeModel create(EmployeeRequestModel request) {

        if (userService.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists.");

        Organization organization = organizationService.findById(request.getOrganizationModel().getId());
        Branch branch = branchService.findById(request.getBranchModel().getId());
        Role role = roleService.findById(request.getRole().getId());
        User user = buildUser(request, organization, branch, role);
        user = userService.create(user);
        Employee employee = buildEmployee(request, role, organization, branch, user);

        employee.setEmployeeCode(
                codeGeneratorService.generateEmployeeCode(
                        organization.getId()
                )
        );
        return employeeTransformer.toModel(employeeService.create(employee));
    }

    public EmployeeModel update(UUID id, EmployeeModel model) {
        Employee employee = employeeService.findById(id);
        if(employee != null){
            employeeService.update(id, model, employee);
        }
        return employeeTransformer.toModel(employee);
    }

    public EmployeeModel delete(UUID id) {
        Employee employee = employeeService.findById(id);
        if(employee != null){
            employeeService.delete(employee);
        }
        return employeeTransformer.toModel(employee);
    }

    public EmployeeModel restore(UUID id) {

        Employee employee = employeeService.findById(id);
        if(employee != null){
            employeeService.restore(employee);
        }
        return employeeTransformer.toModel(employee);
    }

    public EmployeeModel getById(UUID id) {

        return employeeTransformer.toModel(employeeService.findById(id));
    }

    public PageResponse<EmployeeModel> search(EmployeeSearchCriteria criteria, Pageable pageable) {

        PageResponse<Employee> page = employeeService.search(criteria, pageable);

        return PageResponse.<EmployeeModel>builder()
                .content(page.getContent()
                        .stream()
                        .map(employeeTransformer::toModel)
                        .toList())
                .page(page.getPage())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    private User buildUser(EmployeeRequestModel request, Organization organization, Branch branch, Role role) {
        return User.builder()
                .id(UUID.randomUUID())
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .organization(organization)
                .branch(branch)
                .role(role)
                .referralCode(request.getReferralCode())
                .isActive(true)
                .build();
    }

    private Employee buildEmployee(EmployeeRequestModel request, Role role, Organization organization, Branch branch, User user) {
        return Employee.builder()
                .id(UUID.randomUUID())
                .employeeCode("Emp - " + employeeService.count() + 1)
                .fullName(request.getFullName())
                .cnic(request.getCnic())
                .phone(request.getPhone())
                .address(request.getAddress())
                .emergencyContact(request.getEmergencyContact())
                .joiningDate(request.getJoiningDate())
                .salary(BigDecimal.valueOf(request.getSalary()))
                .role(role)
                .organization(organization)
                .branch(branch)
                .user(user)
                .isActive(true)
                .build();
    }

    public EmployeeModel findById(UUID id) {
        Employee employee = employeeService.findById(id);
        if(employee != null)
            return employeeTransformer.toModel(employee);
        return null;
    }
}
