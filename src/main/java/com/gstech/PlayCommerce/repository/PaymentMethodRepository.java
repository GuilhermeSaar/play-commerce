package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.PaymentMethod;
import com.gstech.PlayCommerce.model.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByPaymentType(PaymentType paymentType);

}
