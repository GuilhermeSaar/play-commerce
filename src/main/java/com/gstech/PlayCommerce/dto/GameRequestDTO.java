package com.gstech.PlayCommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GameRequestDTO(

                @NotBlank(message = "Nome é obrigatório")
                String name,

                String description,

                String developer,
                String publisher,
                LocalDate releaseDate,

                @NotNull(message = "Preço é obrigatório") BigDecimal price,
                String classification,

                Boolean available,
                Long categoryId) {
}