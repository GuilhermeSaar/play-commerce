package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.BuyGame;

import java.math.BigDecimal;

public record BuyGameResponseDTO(
        Long gameId,
        String gameName,
        BigDecimal price,
        Integer quantity) {
    public BuyGameResponseDTO(BuyGame buyGame) {
        this(
                buyGame.getGame().getId(),
                buyGame.getGame().getName(),
                buyGame.getPrice(),
                buyGame.getQuantity());
    }
}
