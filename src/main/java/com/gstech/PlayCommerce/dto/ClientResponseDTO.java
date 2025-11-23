package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.enums.UserRole;
import com.gstech.PlayCommerce.model.Client;

import java.time.LocalDateTime;

public record ClientResponseDTO(
        Long id,
        String name,
        String cpf,
        String email,
        String phone,
        LocalDateTime dateRegister,
        UserRole role
) {

    public ClientResponseDTO(Client client) {
        this(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getEmail(),
                client.getPhone(),
                client.getDateRegister(),
                client.getRole()
        );
    }
}

