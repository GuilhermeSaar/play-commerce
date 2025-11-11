package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.exception.DuplicateResourceException;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder encoder;


    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO request) {

        clientRepository.findByCpf(request.cpf())
                .ifPresent(client -> {
                    throw new DuplicateResourceException("Cliente com CPF " + request.cpf() + " já está cadastrado");
                });

        clientRepository.findByEmail(request.email())
                .ifPresent(client -> {
                    throw new DuplicateResourceException("Cliente com email " + request.email() + " já está cadastrado");
                });

        String passwordEncrypted = encoder.encode(request.password());

        var client = new Client(
                request, passwordEncrypted
        );

        return new ClientResponseDTO(clientRepository.save(client));
    }

    @Transactional
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO request) {

        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com id " + id + " não encontrado"));

        if (request.password() != null && !request.password().isBlank()) {
            client.setPassword(encoder.encode(request.password()));
        }

        Optional.ofNullable(request.name()).ifPresent(client::setName);
        Optional.ofNullable(request.phone()).ifPresent(client::setPhone);

        // verifica se existe um email cadastrado de outro cliente
        Optional.ofNullable(request.email()).ifPresent(newEmail -> {
            clientRepository.findByEmail(newEmail)
                    .ifPresent(existingClient -> {
                        if (!existingClient.getId().equals(id)) {
                            throw new DuplicateResourceException("Cliente com email " + newEmail + " já está cadastrado");
                        }
                    });
        });

        // verifica se existe um cpf cadastrado de outro cliente
        Optional.ofNullable(request.cpf()).ifPresent(newCpf -> {
            clientRepository.findByCpf(newCpf)
                    .ifPresent(existingClient -> {
                        if (!existingClient.getId().equals(id)) {
                            throw new DuplicateResourceException("Cliente com CPF " + newCpf + " já está cadastrado");
                        }
                    });
        });
        return new ClientResponseDTO(clientRepository.save(client));
    }
}

