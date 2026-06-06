package com.devmasters.restaurant_erp.menu.domain;

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
@Document(collection = "menu_items")
public class MenuItem extends BaseDomain {

    private String categoryId;

    private String subCategoryId;

    private String itemName;

    private String barcode;

    private String imageUrl;

    private Double salePrice;

    private Double purchasePrice;

    private Integer loyaltyPoints;

    private Double taxPercentage;
}