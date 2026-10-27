package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.Buy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyRepository extends JpaRepository<Buy, Long> {
    List<Buy> findByClientId(Long clientId);
}
