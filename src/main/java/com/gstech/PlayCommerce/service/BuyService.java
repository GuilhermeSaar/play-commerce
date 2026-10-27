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
import com.gstech.PlayCommerce.repository.PaymentMethodRepository;
import com.gstech.PlayCommerce.repository.PaymentStatusRepository;
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
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    @Transactional
    public BuyResponseDTO createBuy(BuyRequest request) {
        validateRequest(request);

        var client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente com id " + request.clientId() + " não encontrado"));

        var paymentMethod = paymentMethodRepository.findByPaymentType(request.payment().paymentMethod())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Forma de pagamento " + request.payment().paymentMethod() + " não encontrada"));

        var paymentStatus = paymentStatusRepository.findByPayStatus(request.payment().paymentStatus())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status de pagamento " + request.payment().paymentStatus() + " não encontrado"));

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

            if (game.getPrice() == null || game.getPrice().compareTo(item.price()) != 0) {
                throw new IllegalArgumentException("O preço informado para o jogo com id " + item.gameId()
                        + " não corresponde ao preço atual");
            }

            buyGames.add(new BuyGame(item, game));
        }

        Payment payment = new Payment(paymentMethod, paymentStatus);
        Buy buy = new Buy(client, request.status(), buyGames, payment);
        client.getBuyList().add(buy);

        return new BuyResponseDTO(buyRepository.save(buy));
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
                || item.quantity() <= 0
                || item.price() == null
                || item.price().signum() < 0) {
            throw new IllegalArgumentException(
                    "Cada item deve informar jogo, quantidade positiva e preço não negativo");
        }
    }

}
