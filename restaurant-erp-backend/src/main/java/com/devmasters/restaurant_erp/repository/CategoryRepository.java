package com.devmasters.restaurant_erp.repository;



import com.devmasters.restaurant_erp.domain.Menu.Category;
import com.devmasters.restaurant_erp.repository.custom.CategoryCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository
        extends MongoRepository<Category, UUID>,
        CategoryCustomRepository {

    boolean existsByCategoryCodeIgnoreCaseAndBranch_Id(
            String categoryCode,
            UUID branchId);

    boolean existsByCategoryNameIgnoreCaseAndBranch_Id(
            String categoryName,
            UUID branchId);
}
