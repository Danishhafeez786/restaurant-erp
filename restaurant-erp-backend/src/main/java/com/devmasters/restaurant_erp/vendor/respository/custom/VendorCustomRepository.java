package com.devmasters.restaurant_erp.vendor.respository.custom;

import com.devmasters.restaurant_erp.vendor.domain.Vendor;
import com.devmasters.restaurant_erp.vendor.model.searchCriteria.VendorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorCustomRepository {

    Page<Vendor> search(
            VendorSearchCriteria criteria,
            Pageable pageable);
}