package com.gstech.PlayCommerce.dto;

import jakarta.validation.constraints.Email;

public record UpdateRequestDTO(

        String name,

        @Email(message = "Email deve ser válido")
        String email,

        String phone,
        String password
) {}
