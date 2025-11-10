package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.exception.DuplicateResourceException;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO requestDTO) {
        // Verifica se já existe cliente com o mesmo CPF
        clientRepository.findByCpf(requestDTO.cpf())
                .ifPresent(client -> {
                    throw new DuplicateResourceException("Cliente com CPF " + requestDTO.cpf() + " já está cadastrado");
                });

        // Verifica se já existe cliente com o mesmo email
        clientRepository.findByEmail(requestDTO.email())
                .ifPresent(client -> {
                    throw new DuplicateResourceException("Cliente com email " + requestDTO.email() + " já está cadastrado");
                });

        // Cria novo cliente
        Client client = new Client();
        client.setName(requestDTO.name());
        client.setCpf(requestDTO.cpf());
        client.setEmail(requestDTO.email());
        client.setPhone(requestDTO.phone());
        client.setDateRegister(LocalDateTime.now());

        // Salva o cliente
        Client savedClient = clientRepository.save(client);

        // Retorna DTO de resposta
        return new ClientResponseDTO(
                savedClient.getId(),
                savedClient.getName(),
                savedClient.getCpf(),
                savedClient.getEmail(),
                savedClient.getPhone(),
                savedClient.getDateRegister()
        );
    }
}

