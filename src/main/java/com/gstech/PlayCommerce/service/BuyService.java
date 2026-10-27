package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.BuyRequest;
import com.gstech.PlayCommerce.dto.BuyGameDTO;
import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Buy;
import com.gstech.PlayCommerce.model.BuyGame;
import com.gstech.PlayCommerce.model.Game;
import com.gstech.PlayCommerce.model.Payment;
import com.gstech.PlayCommerce.repository.BuyRepository;
import com.gstech.PlayCommerce.repository.ClientRepository;
import com.gstech.PlayCommerce.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BuyService {

    private final BuyRepository buyRepository;
    private final ClientRepository clientRepository;
    private final GameRepository gameRepository;

    @Transactional
    public BuyResponseDTO createBuy(BuyRequest request) {
        validateRequest(request);

        var client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente com id " + request.clientId() + " não encontrado"));

        List<BuyGame> buyGames = new ArrayList<>();
        Set<Long> gameIds = new HashSet<>();

        for (BuyGameDTO item : request.items()) {
            validateItem(item);

            if (!gameIds.add(item.gameId())) {
                throw new IllegalArgumentException("O jogo com id " + item.gameId()
                        + " foi informado mais de uma vez na compra");
            }

            Game game = gameRepository.findById(item.gameId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Jogo com id " + item.gameId() + " não encontrado"));

            buyGames.add(new BuyGame(item, game));
        }

        Payment payment = new Payment(request.payment().paymentMethod(), request.payment().paymentStatus());
        Buy buy = new Buy(client, request.status(), buyGames, payment);

        return new BuyResponseDTO(buyRepository.save(buy));
    }

    @Transactional(readOnly = true)
    public List<BuyResponseDTO> getBuysByClient(Long clientId) {
        return buyRepository.findByClientId(clientId).stream()
                .map(BuyResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public BuyResponseDTO getBuyById(Long id) {
        Buy buy = buyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra com id " + id + " não encontrada"));
        return new BuyResponseDTO(buy);
    }

    private void validateRequest(BuyRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Os dados da compra são obrigatórios");
        }

        if (request.clientId() == null) {
            throw new IllegalArgumentException("O cliente da compra é obrigatório");
        }

        if (request.status() == null) {
            throw new IllegalArgumentException("O status da compra é obrigatório");
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("A compra deve conter ao menos um item");
        }

        if (request.payment() == null
                || request.payment().paymentMethod() == null
                || request.payment().paymentStatus() == null) {
            throw new IllegalArgumentException("Os dados de pagamento são obrigatórios");
        }
    }

    private void validateItem(BuyGameDTO item) {
        if (item == null
                || item.gameId() == null
                || item.quantity() == null
                || item.quantity() <= 0) {
            throw new IllegalArgumentException(
                    "Cada item deve informar jogo e quantidade positiva");
        }
    }

}
