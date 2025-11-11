package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.model.Game;
import com.gstech.PlayCommerce.repository.CategoryRepository;
import com.gstech.PlayCommerce.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public GameResponseDTO createGame(GameRequestDTO request) {

        var category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        var game = new Game(
                request,
                category
        );

        return new GameResponseDTO(gameRepository.save(game));
    }

    @Transactional
    public GameResponseDTO updateGame(GameRequestDTO request, Long id) {

        var game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        Optional.ofNullable(request.name()).ifPresent(game::setName);
        Optional.ofNullable(request.description()).ifPresent(game::setDescription);
        Optional.ofNullable(request.developer()).ifPresent(game::setDeveloper);
        Optional.ofNullable(request.publisher()).ifPresent(game::setPublisher);
        Optional.ofNullable(request.releaseDate()).ifPresent(game::setReleaseDate);
        Optional.ofNullable(request.price()).ifPresent(game::setPrice);
        Optional.ofNullable(request.classification()).ifPresent(game::setClassification);
        Optional.ofNullable(request.linkDownload()).ifPresent(game::setLinkDownload);

        Optional.ofNullable(request.categoryId()).ifPresent(categoryId -> {
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            game.setCategory(category);
        });

        return new GameResponseDTO(gameRepository.save(game));
    }

    public void deleteGame(Long id) {
        var game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        gameRepository.delete(game);
    }
}
