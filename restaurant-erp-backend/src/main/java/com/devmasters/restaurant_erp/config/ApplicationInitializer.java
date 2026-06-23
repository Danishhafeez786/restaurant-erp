package com.devmasters.restaurant_erp.config;

import com.devmasters.restaurant_erp.domain.*;
import com.devmasters.restaurant_erp.enums.BillingCycle;
import com.devmasters.restaurant_erp.repository.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements CommandLineRunner {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String developerEmail = "devmasterslearningtech@gmail.com";

        if (userRepository.existsByEmail(developerEmail)) {
            return;
        }

        System.out.println("=================================");
        System.out.println("INITIALIZING Restaurant APPLICATION");
        System.out.println("=================================");

        /*
         * STEP 1
         * Create Subscription Plan
         */
        SubscriptionPlan subscriptionPlan =
                subscriptionPlanRepository.save(
                        SubscriptionPlan.builder()
                                .id(UUID.randomUUID())
                                .name("Enterprise")
                                .branchesLimit(999)
                                .usersLimit(999)
                                .menuItemsLimit(99999)
                                .ordersPerMonth(99999)
                                .monthlyPrice(0.0)
                                .yearlyPrice(0.0)
                                .isActive(true)
                                .build()
                );

        /*
         * STEP 2
         * Create Organization
         */
        Organization organization =
                organizationRepository.save(
                        Organization.builder()
                                .id(UUID.randomUUID())
                                .organizationName("DevMasters Learning Tech")
                                .ownerName("Muhammad Danish")
                                .contactNumber("03440359135")
                                .email("devmasterslearningtech@gmail.com")
                                .address("Near Millat College")
                                .city("Ahmad Pur East")
                                .country("Pakistan")
                                .isActive(true)
                                .subscriptionPlan(subscriptionPlan)
                                .billingCycle(BillingCycle.YEARLY)
                                .subscriptionStartDate(LocalDate.now())
                                .subscriptionEndDate(LocalDate.now().plusYears(1))
                                .build()
                );

        /*
         * STEP 3
         * Create Branch
         */
        Branch branch =
                branchRepository.save(
                        Branch.builder()
                                .id(UUID.randomUUID())
                                .branchName("Head Office")
                                .branchCode("HO-001")
                                .address("Main Office")
                                .city("Ahmad Pur East")
                                .phone("03441234567")
                                .organization(organization)
                                .isActive(true)
                                .build()
                );

        /*
         * STEP 4
         * Create Developer Role
         */
        Role developerRole =
                roleRepository.save(
                        Role.builder()
                                .id(UUID.randomUUID())
                                .roleName("DEVELOPER")
                                .description("System Developer With Full Access")
                                .organization(organization)
                                .isActive(true)
                                .build()
                );

        List<Permission> permissions = permissionRepository.saveAll(
                List.of(

                        permission("DASHBOARD_VIEW","Dashboard View","DASHBOARD"),

                        permission("ORGANIZATION_CREATE","Organization Create","ORGANIZATION"),
                        permission("ORGANIZATION_UPDATE","Organization Update","ORGANIZATION"),

                        permission("BRANCH_CREATE","Branch Create","BRANCH"),
                        permission("BRANCH_UPDATE","Branch Update","BRANCH"),

                        permission("USER_CREATE","User Create","USER"),
                        permission("USER_UPDATE","User Update","USER"),
                        permission("USER_DELETE","User Delete","USER"),

                        permission("ROLE_CREATE","Role Create","ROLE"),
                        permission("ROLE_UPDATE","Role Update","ROLE"),

                        permission("MENU_CREATE","Menu Create","MENU"),
                        permission("MENU_UPDATE","Menu Update","MENU"),

                        permission("ORDER_CREATE","Order Create","ORDER"),
                        permission("ORDER_UPDATE","Order Update","ORDER"),

                        permission("PAYMENT_RECEIVE","Payment Receive","PAYMENT"),

                        permission("REPORT_VIEW","Report View","REPORT"),

                        permission("SUBSCRIPTION_MANAGE","Subscription Manage","SUBSCRIPTION")
                )
        );

        List<RolePermission> rolePermissions = new ArrayList<>();
        for(Permission permission : permissions){
            rolePermissions.add(RolePermission.builder().id(UUID.randomUUID())
                    .role(developerRole).permission(permission).build());
        }

        rolePermissionRepository.saveAll(rolePermissions);

        /*
         * STEP 5
         * Create Developer User
         */
        User developer =
                User.builder()
                        .id(UUID.randomUUID())
                        .username("developer")
                        .password(passwordEncoder.encode("Developer123"))
                        .fullName("Engineer Muhammad")
                        .email(developerEmail)
                        .phone("03440359135")
                        .isActive(true)
                        .organization(organization)
                        .branch(branch)
                        .role(developerRole)
                        .build();

        userRepository.save(developer);

        System.out.println("=================================");
        System.out.println("DevMasters Learning Tech INITIALIZATION COMPLETED");
        System.out.println("=================================");
        System.out.println("Username : developer");
        System.out.println("Email    : devmasterslearningtech@gmail.com");
        System.out.println("Password : Developer123");
        System.out.println("Organization : " + organization.getOrganizationName());
        System.out.println("Branch       : " + branch.getBranchName());
        System.out.println("=================================");
    }

    private Permission permission(String code, String name, String module) {

        return Permission.builder()
                .id(UUID.randomUUID())
                .code(code)
                .name(name)
                .module(module)
                .build();
    }
}