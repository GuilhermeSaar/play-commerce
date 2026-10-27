package com.gstech.PlayCommerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GameRequestDTO(

                @NotBlank(message = "Nome é obrigatório")
                String name,

                @NotBlank(message = "Descrição é obrigatória")
                String description,

                String developer,

                @NotBlank(message = "Publicadora é obrigatória")
                String publisher,

                @NotNull(message = "Data de lançamento é obrigatória")
                LocalDate releaseDate,

                @NotNull(message = "Preço é obrigatório")
                @DecimalMin(value = "0.00", message = "Preço não pode ser negativo")
                BigDecimal price,

                @NotNull(message = "Classificação é obrigatória")
                @Min(value = 0, message = "Classificação deve estar entre 0 e 18")
                @Max(value = 18, message = "Classificação deve estar entre 0 e 18")
                Integer classification,

                @NotNull(message = "Categoria é obrigatória")
                Long categoryId
) {
}
