package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.BuyGameDTO;
import com.gstech.PlayCommerce.dto.BuyRequest;
import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.dto.PaymentDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Buy;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.model.Game;
import com.gstech.PlayCommerce.model.enums.BuyStatus;
import com.gstech.PlayCommerce.model.enums.PayStatus;
import com.gstech.PlayCommerce.model.enums.PaymentType;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyServiceTest {

    @Mock
    private BuyRepository buyRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private BuyService buyService;

    private Client client;
    private Game game;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);

        game = new Game();
        game.setId(1L);
        game.setName("Game");
        game.setPrice(new BigDecimal("59.99"));
    }

    @Test
    void shouldCreateBuyWithPaymentAndItems() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(buyRepository.save(any(Buy.class))).thenAnswer(invocation -> {
            Buy buy = invocation.getArgument(0, Buy.class);
            buy.setId(1L);
            return buy;
        });

        BuyResponseDTO response = buyService.createBuy(request(List.of(new BuyGameDTO(1L, 1))));

        assertEquals(1L, response.id());
        assertEquals(1, response.games().size());
        assertEquals(new BigDecimal("59.99"), response.games().get(0).price());
        assertEquals("PIX", response.payment().paymentMethod());
        verify(buyRepository).save(any(Buy.class));
    }

    @Test
    void shouldRejectUnknownClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> buyService.createBuy(request(List.of(new BuyGameDTO(1L, 1)))));

        assertEquals("Cliente com id 1 não encontrado", exception.getMessage());
    }

    @Test
    void shouldRejectUnknownGame() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> buyService.createBuy(request(List.of(new BuyGameDTO(999L, 1)))));

        assertEquals("Jogo com id 999 não encontrado", exception.getMessage());
        verify(buyRepository, never()).save(any());
    }

    @Test
    void shouldRejectDuplicateGames() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> buyService.createBuy(request(List.of(
                        new BuyGameDTO(1L, 1),
                        new BuyGameDTO(1L, 2)))));

        assertEquals("O jogo com id 1 foi informado mais de uma vez na compra", exception.getMessage());
    }

    @Test
    void shouldReturnBuyHistory() {
        Buy buy = new Buy();
        buy.setId(1L);
        buy.setClient(client);
        when(buyRepository.findByClientId(1L)).thenReturn(List.of(buy));

        List<BuyResponseDTO> response = buyService.getBuysByClient(1L);

        assertEquals(1, response.size());
    }

    private BuyRequest request(List<BuyGameDTO> items) {
        return new BuyRequest(1L, BuyStatus.COMPLETED, items,
                new PaymentDTO(PaymentType.PIX, PayStatus.APROVADO));
    }
}
