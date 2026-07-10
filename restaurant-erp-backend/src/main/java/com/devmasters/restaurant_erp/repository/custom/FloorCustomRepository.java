package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Floor;
import com.devmasters.restaurant_erp.model.searchcriteria.FloorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FloorCustomRepository {

    Page<Floor> search(
            FloorSearchCriteria criteria,
            Pageable pageable
    );
}
