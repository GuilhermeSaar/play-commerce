package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.enums.BuyStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BuyRequest(
        @NotNull(message = "Cliente é obrigatório")
        Long clientId,

        @NotNull(message = "Status é obrigatório")
        BuyStatus status,

        @NotEmpty(message = "A compra deve conter ao menos um item")
        List<@Valid BuyGameDTO> items,

        @NotNull(message = "Pagamento é obrigatório")
        @Valid
        PaymentDTO payment) {
}
