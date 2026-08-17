package com.devmasters.restaurant_erp.floor.respository.custom;

import com.devmasters.restaurant_erp.floor.domain.Floor;
import com.devmasters.restaurant_erp.floor.model.searchCriteria.FloorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FloorCustomRepository {

    Page<Floor> search(
            FloorSearchCriteria criteria,
            Pageable pageable
    );
}
