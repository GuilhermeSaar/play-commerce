package com.gstech.PlayCommerce.dto;

import com.gstech.PlayCommerce.model.Game;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GameResponseDTO(
        Long id,
        String name,
        String description,
        String developer,
        String publisher,
        LocalDate releaseDate,
        BigDecimal price,
        String classification,
        String linkDownload,
        Long categoryId
) {
    public GameResponseDTO(Game game) {
        this(
                game.getId(),
                game.getName(),
                game.getDescription(),
                game.getDeveloper(),
                game.getPublisher(),
                game.getReleaseDate(),
                game.getPrice(),
                game.getClassification(),
                game.getLinkDownload(),
                game.getCategory().getId()
        );
    }
}