package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.PaymentStatus;
import com.gstech.PlayCommerce.model.enums.PayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<com.gstech.PlayCommerce.model.PaymentStatus, Long> {

    Optional<PaymentStatus> findByPayStatus(PayStatus payStatus);

}
