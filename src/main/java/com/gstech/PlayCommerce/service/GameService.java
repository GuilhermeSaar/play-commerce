package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Game;
import com.gstech.PlayCommerce.repository.CategoryRepository;
import com.gstech.PlayCommerce.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;

    public GameService(GameRepository gameRepository, CategoryRepository categoryRepository) {
        this.gameRepository = gameRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public GameResponseDTO createGame(GameRequestDTO request) {

        var category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        var game = new Game(
                request,
                category);

        return new GameResponseDTO(gameRepository.save(game));
    }

    @Transactional
    public GameResponseDTO updateGame(GameRequestDTO request, Long id) {

        var game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));

        Optional.ofNullable(request.name()).ifPresent(game::setName);
        Optional.ofNullable(request.description()).ifPresent(game::setDescription);
        Optional.ofNullable(request.developer()).ifPresent(game::setDeveloper);
        Optional.ofNullable(request.publisher()).ifPresent(game::setPublisher);
        Optional.ofNullable(request.releaseDate()).ifPresent(game::setReleaseDate);
        Optional.ofNullable(request.price()).ifPresent(game::setPrice);
        Optional.ofNullable(request.classification()).ifPresent(game::setClassification);

        Optional.ofNullable(request.categoryId()).ifPresent(categoryId -> {
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
            game.setCategory(category);
        });

        return new GameResponseDTO(gameRepository.save(game));
    }

    public void deleteGame(Long id) {
        var game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));
        gameRepository.delete(game);
    }

    public java.util.List<GameResponseDTO> findAllGames() {
        return gameRepository.findAll().stream()
                .map(GameResponseDTO::new)
                .toList();
    }

    public GameResponseDTO findGameById(Long id) {
        var game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));
        return new GameResponseDTO(game);
    }
}
