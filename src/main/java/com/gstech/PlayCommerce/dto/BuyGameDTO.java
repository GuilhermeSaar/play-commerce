package com.gstech.PlayCommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuyGameDTO(
        @NotNull(message = "Jogo é obrigatório")
        Long gameId,

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser positiva")
        Integer quantity) {
}
