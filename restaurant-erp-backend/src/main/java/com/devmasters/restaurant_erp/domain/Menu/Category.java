package com.devmasters.restaurant_erp.domain.Menu;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("categories")
public class Category extends BaseEntity {

    private String categoryCode;
    private String categoryName;
    private String description;
    private String imageUrl;
    private Integer displayOrder;

    @Builder.Default
    private Boolean available = true;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}
