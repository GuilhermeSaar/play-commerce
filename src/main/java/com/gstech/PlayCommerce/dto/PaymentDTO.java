package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.enums.PayStatus;
import com.gstech.PlayCommerce.model.enums.PaymentType;
import jakarta.validation.constraints.NotNull;

public record PaymentDTO(
        @NotNull(message = "Forma de pagamento é obrigatória")
        PaymentType paymentMethod,

        @NotNull(message = "Status do pagamento é obrigatório")
        PayStatus paymentStatus
) {
}
