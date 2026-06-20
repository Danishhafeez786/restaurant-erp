package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchRequest {

    private int pageNumber = 0;
    private int pageSize = 10;

    private String sortBy = "createdAt";
    private String sortDirection = "DESC";

    private String keyword;

    private Role role;
}
