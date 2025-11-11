package com.gstech.PlayCommerce.repository;

import com.gstech.PlayCommerce.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByCpf(String cpf);
    Optional<Client> findByEmail(String email);
}

