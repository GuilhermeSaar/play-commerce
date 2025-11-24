package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.dto.UpdateRequestDTO;
import com.gstech.PlayCommerce.exception.DuplicateResourceException;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder encoder;


    public ClientService(ClientRepository clientRepository, PasswordEncoder encoder) {
        this.clientRepository = clientRepository;
        this.encoder = encoder;
    }

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO request) {

        clientRepository.findByCpf(request.cpf())
                .ifPresent(client -> {
                    throw new DuplicateResourceException("Cliente com CPF " + request.cpf() + " já está cadastrado");
                });

        clientRepository.findByEmail(request.email())
                .ifPresent(client -> {
                    throw new DuplicateResourceException(
                            "Cliente com email " + request.email() + " já está cadastrado");
                });

        String passwordEncrypted = encoder.encode(request.password());

        var client = new Client(
                request, passwordEncrypted);

        return new ClientResponseDTO(clientRepository.save(client));
    }

    @Transactional
    public ClientResponseDTO updateClient(Long id, UpdateRequestDTO request) {

        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com id " + id + " não encontrado"));

        if (request.password() != null && !request.password().isBlank()) {
            client.setPassword(encoder.encode(request.password()));
        }

        if (request.name() != null && !request.name().isBlank()) {
            client.setName(request.name());
        }

        if (request.phone() != null && !request.phone().isBlank()) {
            client.setPhone(request.phone());
        }

        if (request.email() != null && !request.email().isBlank()) {
            var newEmail = request.email();

            clientRepository.findByEmail(newEmail)
                    .ifPresent(existingClient -> {
                        if (!existingClient.getId().equals(id)) {
                            throw new DuplicateResourceException(
                                    "Cliente com email " + newEmail + " já está cadastrado");
                        }
                    });

            client.setEmail(newEmail);
        }

        return new ClientResponseDTO(clientRepository.save(client));
    }

    @Transactional(readOnly = true)
    public List<GameResponseDTO> getLibrary(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com id " + clientId + " não encontrado"));

        return client.getGames().stream()
                .map(GameResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String getDownloadLink(Long clientId, Long gameId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com id " + clientId + " não encontrado"));

        return client.getGames().stream()
                .filter(game -> game.getId().equals(gameId))
                .findFirst()
                .map(game -> game.getLinkDownload())
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado na biblioteca do cliente"));
    }
}
