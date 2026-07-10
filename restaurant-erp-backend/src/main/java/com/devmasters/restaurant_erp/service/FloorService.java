package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Floor;
import com.devmasters.restaurant_erp.model.searchcriteria.FloorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FloorService {

    boolean existsByFloorNameIgnoreCaseAndBranch_Id(String floorName, UUID branchId);
    Floor create(Floor entity);
    Page<Floor> search(FloorSearchCriteria criteria, Pageable pageable);

    Floor findById(UUID id);

    Floor update(UUID id, Floor entity);

    Floor delete(UUID id);

    Floor restore(UUID id);
}
