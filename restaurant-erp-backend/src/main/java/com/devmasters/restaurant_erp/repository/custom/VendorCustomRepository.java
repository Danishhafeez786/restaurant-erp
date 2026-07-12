package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Vendor;
import com.devmasters.restaurant_erp.model.searchcriteria.VendorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorCustomRepository {

    Page<Vendor> search(
            VendorSearchCriteria criteria,
            Pageable pageable);
}