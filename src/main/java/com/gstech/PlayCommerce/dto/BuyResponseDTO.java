package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.Buy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BuyResponseDTO(Long id, Long clientId, List<BuyGameResponseDTO> games, BigDecimal totalPrice,
                LocalDateTime date,
                PaymentResponseDTO payment) {

        public BuyResponseDTO(Buy buy) {
                this(
                                buy.getId(),
                                buy.getClient().getId(),
                                buy.getBuyGames().stream().map(BuyGameResponseDTO::new).toList(),
                                buy.getBuyGames().stream()
                                                .map(bg -> bg.getPrice().multiply(BigDecimal.valueOf(bg.getQuantity())))
                                                .reduce(BigDecimal.ZERO, BigDecimal::add),
                                buy.getDate(),
                                buy.getPayment() != null ? new PaymentResponseDTO(buy.getPayment()) : null);
        }
}
