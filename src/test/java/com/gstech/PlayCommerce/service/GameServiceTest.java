package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Category;
import com.gstech.PlayCommerce.model.Game;
import com.gstech.PlayCommerce.repository.CategoryRepository;
import com.gstech.PlayCommerce.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GameService gameService;

    private Category category;
    private Game game;
    private GameRequestDTO request;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("RPG");

        request = new GameRequestDTO("Game", "Description", "Studio", "Publisher",
                LocalDate.of(2024, 1, 1), BigDecimal.TEN, 18, 1L);
        game = new Game(request, category);
        game.setId(1L);
    }

    @Test
    void shouldCreateGameWhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        GameResponseDTO response = gameService.createGame(request);

        assertEquals(1L, response.id());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void shouldRejectUnknownCategoryOnCreate() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> gameService.createGame(request));

        assertEquals("Categoria não encontrada", exception.getMessage());
    }

    @Test
    void shouldListGames() {
        when(gameRepository.findAll()).thenReturn(List.of(game));

        List<GameResponseDTO> games = gameService.findAllGames();

        assertEquals(1, games.size());
        assertEquals("Game", games.get(0).name());
    }

    @Test
    void shouldFindGameById() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        GameResponseDTO response = gameService.findGameById(1L);

        assertEquals(1L, response.id());
    }

    @Test
    void shouldRejectUnknownGame() {
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> gameService.findGameById(999L));

        assertEquals("Jogo não encontrado", exception.getMessage());
    }
}
