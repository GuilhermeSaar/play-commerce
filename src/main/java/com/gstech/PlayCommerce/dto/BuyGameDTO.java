package com.gstech.PlayCommerce.dto;

import java.math.BigDecimal;

public record BuyGameDTO(
        Long gameId,
        Integer quantity,
        BigDecimal price) {
}
