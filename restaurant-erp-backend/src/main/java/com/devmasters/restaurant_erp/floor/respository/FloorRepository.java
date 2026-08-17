package com.devmasters.restaurant_erp.floor.respository;

import com.devmasters.restaurant_erp.floor.domain.Floor;
import com.devmasters.restaurant_erp.floor.respository.custom.FloorCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FloorRepository
        extends MongoRepository<Floor, UUID>,
        FloorCustomRepository {

    boolean existsByFloorNameIgnoreCaseAndBranch_Id(
            String floorName,
            UUID branchId
    );
}
