package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.Buy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyRepository extends JpaRepository<Buy, Long> {
    List<Buy> findByClientId(Long clientId);
}
