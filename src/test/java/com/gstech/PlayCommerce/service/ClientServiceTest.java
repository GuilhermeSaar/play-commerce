package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.exception.DuplicateResourceException;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new ClientRequestDTO("John Doe", "12345678901", "john@example.com", "11999999999");
    }

    @Test
    void shouldCreateClientWhenUniqueFieldsAreAvailable() {
        Client savedClient = new Client(request);
        savedClient.setId(1L);
        when(clientRepository.findByCpf(request.cpf())).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(clientRepository.findByPhone(request.phone())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponseDTO response = clientService.createClient(request);

        assertEquals(1L, response.id());
        assertEquals(request.email(), response.email());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void shouldRejectDuplicateCpf() {
        when(clientRepository.findByCpf(request.cpf())).thenReturn(Optional.of(new Client()));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> clientService.createClient(request));

        assertEquals("Cliente com CPF 12345678901 já está cadastrado", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(clientRepository.findByCpf(request.cpf())).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(request.email())).thenReturn(Optional.of(new Client()));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> clientService.createClient(request));

        assertEquals("Cliente com email john@example.com já está cadastrado", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldRejectDuplicatePhone() {
        when(clientRepository.findByCpf(request.cpf())).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(clientRepository.findByPhone(request.phone())).thenReturn(Optional.of(new Client()));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> clientService.createClient(request));

        assertEquals("Cliente com telefone 11999999999 já está cadastrado", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }
}
