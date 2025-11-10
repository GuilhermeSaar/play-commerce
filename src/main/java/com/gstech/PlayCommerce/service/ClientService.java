package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.exception.DuplicateResourceException;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
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

    @Transactional
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO requestDTO) {
        // Busca o cliente pelo ID
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado"));

        // Verifica se já existe outro cliente com o mesmo CPF (exceto o próprio)
        clientRepository.findByCpf(requestDTO.cpf())
                .ifPresent(existingClient -> {
                    if (!existingClient.getId().equals(id)) {
                        throw new DuplicateResourceException("Cliente com CPF " + requestDTO.cpf() + " já está cadastrado");
                    }
                });

        // Verifica se já existe outro cliente com o mesmo email (exceto o próprio)
        clientRepository.findByEmail(requestDTO.email())
                .ifPresent(existingClient -> {
                    if (!existingClient.getId().equals(id)) {
                        throw new DuplicateResourceException("Cliente com email " + requestDTO.email() + " já está cadastrado");
                    }
                });

        // Atualiza os dados do cliente (mantém a data de registro original)
        client.setName(requestDTO.name());
        client.setCpf(requestDTO.cpf());
        client.setEmail(requestDTO.email());
        client.setPhone(requestDTO.phone());

        // Salva o cliente atualizado
        Client updatedClient = clientRepository.save(client);

        // Retorna DTO de resposta
        return new ClientResponseDTO(
                updatedClient.getId(),
                updatedClient.getName(),
                updatedClient.getCpf(),
                updatedClient.getEmail(),
                updatedClient.getPhone(),
                updatedClient.getDateRegister()
        );
    }
}

