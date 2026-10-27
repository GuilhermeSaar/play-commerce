package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByCpf(String cpf);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);
}

