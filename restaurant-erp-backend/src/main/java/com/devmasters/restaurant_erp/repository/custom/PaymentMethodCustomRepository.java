package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.PaymentMethod;
import com.devmasters.restaurant_erp.model.searchcriteria.PaymentMethodSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentMethodCustomRepository {

    Page<PaymentMethod> search(PaymentMethodSearchCriteria criteria, Pageable pageable);
}