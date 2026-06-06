package com.devmasters.restaurant_erp.user.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import com.devmasters.restaurant_erp.common.enums.UserStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User extends BaseDomain {

    private String organizationId;

    private String branchId;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String password;

    private String roleId;

    private UserStatus status;
}
