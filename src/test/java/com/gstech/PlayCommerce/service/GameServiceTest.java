package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private GameService gameService;

    private GameRequestDTO validRequest;
    private Category category;
    private Game game;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Action");

        validRequest = new GameRequestDTO(
                "Super Game",
                "An awesome game",
                "GameDev Studio",
                "Publisher Inc",
                LocalDate.of(2024, 1, 15),
                new BigDecimal("59.99"),
                "18+",
                "https://download.link/game",
                true,
                1L);

        game = new Game();
        game.setId(1L);
        game.setName("Super Game");
        game.setDescription("An awesome game");
        game.setDeveloper("GameDev Studio");
        game.setPublisher("Publisher Inc");
        game.setReleaseDate(LocalDate.of(2024, 1, 15));
        game.setPrice(new BigDecimal("59.99"));
        game.setClassification("18+");
        game.setLinkDownload("https://download.link/game");
        game.setAvailable(true);
        game.setCategory(category);
    }

    @Test
    void shouldCreateGameSuccessfully_WhenCategoryExists() {

        when(categoryRepository.findById(validRequest.categoryId())).thenReturn(Optional.of(category));
        when(gameRepository.save(any(Game.class))).thenReturn(game);


        GameResponseDTO response = gameService.createGame(validRequest);


        assertNotNull(response);
        verify(categoryRepository, times(1)).findById(validRequest.categoryId());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void shouldThrowRuntimeException_WhenCategoryNotFoundOnCreate() {

        Long invalidCategoryId = 999L;
        GameRequestDTO requestWithInvalidCategory = new GameRequestDTO(
                "Super Game",
                "An awesome game",
                "GameDev Studio",
                "Publisher Inc",
                LocalDate.of(2024, 1, 15),
                new BigDecimal("59.99"),
                "18+",
                "https://download.link/game",
                true,
                invalidCategoryId);

        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> gameService.createGame(requestWithInvalidCategory));

        assertEquals("Categoria não encontrada", exception.getMessage());
        verify(categoryRepository, times(1)).findById(invalidCategoryId);
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void shouldUpdateGameSuccessfully_WhenGameExists() {

        Long gameId = 1L;
        GameRequestDTO updateRequest = new GameRequestDTO(
                "Updated Game Name",
                "Updated description",
                null,
                null,
                null,
                new BigDecimal("49.99"),
                null,
                null,
                null,
                null);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);


        GameResponseDTO response = gameService.updateGame(updateRequest, gameId);


        assertNotNull(response);
        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    void shouldUpdateGameCategory_WhenNewCategoryIsProvided() {

        Long gameId = 1L;
        Long newCategoryId = 2L;
        Category newCategory = new Category();
        newCategory.setId(newCategoryId);
        newCategory.setName("Adventure");

        GameRequestDTO updateRequest = new GameRequestDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                newCategoryId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(categoryRepository.findById(newCategoryId)).thenReturn(Optional.of(newCategory));
        when(gameRepository.save(any(Game.class))).thenReturn(game);


        GameResponseDTO response = gameService.updateGame(updateRequest, gameId);


        assertNotNull(response);
        verify(gameRepository, times(1)).findById(gameId);
        verify(categoryRepository, times(1)).findById(newCategoryId);
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void shouldThrowRuntimeException_WhenGameNotFoundOnUpdate() {

        Long invalidGameId = 999L;
        when(gameRepository.findById(invalidGameId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> gameService.updateGame(validRequest, invalidGameId));

        assertEquals("Jogo não encontrado", exception.getMessage());
        verify(gameRepository, times(1)).findById(invalidGameId);
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void shouldThrowRuntimeException_WhenCategoryNotFoundOnUpdate() {

        Long gameId = 1L;
        Long invalidCategoryId = 999L;
        GameRequestDTO updateRequest = new GameRequestDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                invalidCategoryId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(categoryRepository.findById(invalidCategoryId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> gameService.updateGame(updateRequest, gameId));

        assertEquals("Categoria não encontrada", exception.getMessage());
        verify(gameRepository, times(1)).findById(gameId);
        verify(categoryRepository, times(1)).findById(invalidCategoryId);
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void shouldDeleteGameSuccessfully_WhenGameExists() {

        Long gameId = 1L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        doNothing().when(gameRepository).delete(game);


        gameService.deleteGame(gameId);


        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, times(1)).delete(game);
    }

    @Test
    void shouldThrowRuntimeException_WhenGameNotFoundOnDelete() {

        Long invalidGameId = 999L;
        when(gameRepository.findById(invalidGameId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> gameService.deleteGame(invalidGameId));

        assertEquals("Jogo não encontrado", exception.getMessage());
        verify(gameRepository, times(1)).findById(invalidGameId);
        verify(gameRepository, never()).delete(any(Game.class));
    }
}
