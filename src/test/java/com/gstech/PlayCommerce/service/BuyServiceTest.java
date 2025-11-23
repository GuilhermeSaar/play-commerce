package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.BuyRequestDTO;
import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.dto.PaymentRequestDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.enums.PaymentStatusType;
import com.gstech.PlayCommerce.model.enums.PaymentType;
import com.gstech.PlayCommerce.model.Buy;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.model.Game;
import com.gstech.PlayCommerce.repository.BuyRepository;
import com.gstech.PlayCommerce.repository.ClientRepository;
import com.gstech.PlayCommerce.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyServiceTest {

    @Mock
    private BuyRepository buyRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private BuyService buyService;

    private Client client;
    private Game game1;
    private Game game2;
    private Buy buy;

    @BeforeEach
    void setUp() {

        client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setEmail("test@example.com");

        game1 = new Game();
        game1.setId(1L);
        game1.setName("Game 1");
        game1.setPrice(new BigDecimal("59.99"));
        game1.setAvailable(true);

        game2 = new Game();
        game2.setId(2L);
        game2.setName("Game 2");
        game2.setPrice(new BigDecimal("49.99"));
        game2.setAvailable(true);

        buy = new Buy();
        buy.setId(1L);
        buy.setClient(client);
    }

    @Test
    void shouldCreateBuySuccessfully_WhenClientAndGamesExist() {

        Long clientId = 1L;
        Long gameId = 1L;
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.PIX, PaymentStatusType.APROVADO);
        BuyRequestDTO request = new BuyRequestDTO(clientId, List.of(gameId), paymentRequest);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game1));
        when(buyRepository.save(any(Buy.class))).thenReturn(buy);

        BuyResponseDTO response = buyService.createBuy(request);

        assertNotNull(response);
        verify(clientRepository, times(1)).findById(clientId);
        verify(gameRepository, times(1)).findById(gameId);
        verify(buyRepository, times(1)).save(any(Buy.class));
    }

    @Test
    void shouldCreateBuyWithMultipleGames_WhenAllGamesAreAvailable() {

        Long clientId = 1L;
        List<Long> gameIds = Arrays.asList(1L, 2L);
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.CARTAO_CREDITO,
                PaymentStatusType.APROVADO);
        BuyRequestDTO request = new BuyRequestDTO(clientId, gameIds, paymentRequest);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game1));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(game2));
        when(buyRepository.save(any(Buy.class))).thenReturn(buy);

        BuyResponseDTO response = buyService.createBuy(request);

        assertNotNull(response);
        verify(clientRepository, times(1)).findById(clientId);
        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).findById(2L);
        verify(buyRepository, times(1)).save(any(Buy.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenClientNotFound() {

        Long clientId = 999L;
        Long gameId = 1L;
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.PAYPAL, PaymentStatusType.PENDENTE);
        BuyRequestDTO request = new BuyRequestDTO(clientId, List.of(gameId), paymentRequest);

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> buyService.createBuy(request));

        assertTrue(exception.getMessage().contains("Cliente com id " + clientId + " não encontrado"));
        verify(clientRepository, times(1)).findById(clientId);
        verify(gameRepository, never()).findById(any());
        verify(buyRepository, never()).save(any(Buy.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenGameNotFound() {

        Long clientId = 1L;
        Long invalidGameId = 999L;
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.BOLETO, PaymentStatusType.RECUSADO);
        BuyRequestDTO request = new BuyRequestDTO(clientId, List.of(invalidGameId), paymentRequest);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(gameRepository.findById(invalidGameId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> buyService.createBuy(request));

        assertTrue(exception.getMessage().contains("Jogo com id " + invalidGameId + " não encontrado"));
        verify(clientRepository, times(1)).findById(clientId);
        verify(gameRepository, times(1)).findById(invalidGameId);
        verify(buyRepository, never()).save(any(Buy.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenGameIsNotAvailable() {

        Long clientId = 1L;
        Long gameId = 1L;
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(PaymentType.PIX, PaymentStatusType.PENDENTE);
        BuyRequestDTO request = new BuyRequestDTO(clientId, List.of(gameId), paymentRequest);

        game1.setAvailable(false);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game1));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> buyService.createBuy(request));

        assertTrue(exception.getMessage().contains("Jogo com id " + gameId + " não está disponível"));
        verify(clientRepository, times(1)).findById(clientId);
        verify(gameRepository, times(1)).findById(gameId);
        verify(buyRepository, never()).save(any(Buy.class));
    }
}
