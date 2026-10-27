package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.enums.BuyStatus;

import java.util.List;

public record BuyRequest(
        Long clientId,
        BuyStatus status,
        List<BuyGameDTO> items,
        PaymentDTO payment) {
}
