package com.gstech.PlayCommerce.dto;

public record LoginResponse(
        String token,
        Long expiresIn
) {
}
