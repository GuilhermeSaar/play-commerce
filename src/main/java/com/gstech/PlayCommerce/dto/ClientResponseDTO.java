package com.gstech.PlayCommerce.dto;

import java.time.LocalDateTime;

public record ClientResponseDTO(
        Long id,
        String name,
        String cpf,
        String email,
        String phone,
        LocalDateTime dateRegister
) {
}

