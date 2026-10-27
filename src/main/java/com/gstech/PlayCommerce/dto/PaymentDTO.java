package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.enums.PayStatus;
import com.gstech.PlayCommerce.model.enums.PaymentType;

public record PaymentDTO(
        PaymentType paymentMethod,
        PayStatus paymentStatus
) {
}
