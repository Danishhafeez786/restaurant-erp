package com.devmasters.restaurant_erp.auth.model.searchCriteria;

import com.devmasters.restaurant_erp.role.domain.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchRequest {

    @Min(value = 0, message = "Page number cannot be negative")
    private int pageNumber = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private int pageSize = 10;

    @Size(max = 50, message = "Sort by cannot exceed 50 characters")
    private String sortBy = "createdAt";

    @Pattern(
            regexp = "ASC|DESC",
            message = "Sort direction must be either ASC or DESC"
    )
    private String sortDirection = "DESC";

    @Size(max = 100, message = "Keyword cannot exceed 100 characters")
    private String keyword;

    private Role role;
}
