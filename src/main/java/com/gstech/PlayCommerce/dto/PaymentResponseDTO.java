package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.Payment;

import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long id,
        String paymentMethod,
        String paymentStatus,
        LocalDateTime paymentDate,
        Long buyId,
        Long clientId) {

    public PaymentResponseDTO(Payment payment) {
        this(
                payment.getId(),
                payment.getPaymentMethod().getPaymentType().name(),
                payment.getPaymentStatus().getStatus().name(),
                payment.getPaymentDate(),
                payment.getBuy().getId(),
                payment.getClient().getId());
    }
}
