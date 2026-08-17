package com.devmasters.restaurant_erp.paymentmethod.respository.custom;

import com.devmasters.restaurant_erp.paymentmethod.domain.PaymentMethod;
import com.devmasters.restaurant_erp.paymentmethod.model.searchCriteria.PaymentMethodSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentMethodCustomRepository {

    Page<PaymentMethod> search(PaymentMethodSearchCriteria criteria, Pageable pageable);
}