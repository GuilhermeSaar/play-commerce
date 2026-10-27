package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.exception.DuplicateResourceException;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClientService {

    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
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

        clientRepository.findByPhone(request.phone())
                .ifPresent(client -> {
                    throw new DuplicateResourceException(
                            "Cliente com telefone " + request.phone() + " já está cadastrado");
                });

        var client = new Client(request);

        return new ClientResponseDTO(clientRepository.save(client));
    }
}
