package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.BuyRequestDTO;
import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Buy;
import com.gstech.PlayCommerce.model.BuyGame;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.model.Game;
import com.gstech.PlayCommerce.model.Payment;
import com.gstech.PlayCommerce.repository.BuyRepository;
import com.gstech.PlayCommerce.repository.ClientRepository;
import com.gstech.PlayCommerce.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuyService {

    private final BuyRepository buyRepository;
    private final ClientRepository clientRepository;
    private final GameRepository gameRepository;
    private final PaymentService paymentService;

    public BuyService(BuyRepository buyRepository, ClientRepository clientRepository,
            GameRepository gameRepository, PaymentService paymentService) {
        this.buyRepository = buyRepository;
        this.clientRepository = clientRepository;
        this.gameRepository = gameRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public BuyResponseDTO createBuy(BuyRequestDTO request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente com id " + request.clientId() + " não encontrado"));

        Buy buy = new Buy();
        buy.setClient(client);
        buy.setDate(LocalDateTime.now());
        buy.setStatus("COMPLETED");

        List<BuyGame> buyGames = new ArrayList<>();
        for (Long gameId : request.gameIds()) {
            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new ResourceNotFoundException("Jogo com id " + gameId + " não encontrado"));

            if (!game.isAvailable()) {
                throw new ResourceNotFoundException("Jogo com id " + gameId + " não está disponível");
            }

            BuyGame buyGame = new BuyGame();
            buyGame.setBuy(buy);
            buyGame.setGame(game);
            buyGame.setPrice(game.getPrice());
            buyGame.setQuantity(1); // 1 unidade por compra

            buyGames.add(buyGame);
        }

        buy.setBuyGames(buyGames);
        Buy savedBuy = buyRepository.save(buy);

        for (BuyGame bg : buyGames) {
            client.getGames().add(bg.getGame());
        }
        clientRepository.save(client);

        Payment payment = paymentService.createPayment(savedBuy, client, request.payment());
        savedBuy.setPayment(payment);

        return new BuyResponseDTO(savedBuy);
    }
}
