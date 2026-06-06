package com.devmasters.restaurant_erp.category.domain;

import com.devmasters.restaurant_erp.common.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "categories")
public class Category extends BaseDomain {

    private String organizationId;

    private String name;

    private String description;
}