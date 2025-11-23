package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.enums.PaymentStatusType;
import com.gstech.PlayCommerce.model.enums.PaymentType;

public record PaymentRequestDTO(PaymentType paymentMethod, PaymentStatusType paymentStatus) {
}
