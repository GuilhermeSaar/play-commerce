package com.gstech.PlayCommerce.service;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.exception.DuplicateResourceException;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.model.Client;
import com.gstech.PlayCommerce.repository.ClientRepository;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

        @Mock
        private ClientRepository clientRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @InjectMocks
        private ClientService clientService;

        private ClientRequestDTO validRequest;
        private Client client;

        @BeforeEach
        void setUp() {
                validRequest = new ClientRequestDTO(
                                "John Doe",
                                "12345678901",
                                "john.doe@example.com",
                                "11987654321",
                                "password123");

                client = new Client();
                client.setId(1L);
                client.setName("John Doe");
                client.setCpf("12345678901");
                client.setEmail("john.doe@example.com");
                client.setPhone("11987654321");
                client.setPassword("encodedPassword");
        }

        @Test
        void shouldCreateClientSuccessfully_WhenDataIsValid() {

                when(clientRepository.findByCpf(validRequest.cpf())).thenReturn(Optional.empty());
                when(clientRepository.findByEmail(validRequest.email())).thenReturn(Optional.empty());
                when(passwordEncoder.encode(validRequest.password())).thenReturn("encodedPassword");
                when(clientRepository.save(any(Client.class))).thenReturn(client);

                ClientResponseDTO response = clientService.createClient(validRequest);

                assertNotNull(response);
                verify(clientRepository, times(1)).findByCpf(validRequest.cpf());
                verify(clientRepository, times(1)).findByEmail(validRequest.email());
                verify(passwordEncoder, times(1)).encode(validRequest.password());
                verify(clientRepository, times(1)).save(any(Client.class));
        }

        @Test
        void shouldThrowDuplicateResourceException_WhenCpfAlreadyExists() {

                when(clientRepository.findByCpf(validRequest.cpf())).thenReturn(Optional.of(client));

                DuplicateResourceException exception = assertThrows(
                                DuplicateResourceException.class,
                                () -> clientService.createClient(validRequest));

                assertTrue(exception.getMessage()
                                .contains("Cliente com CPF " + validRequest.cpf() + " já está cadastrado"));
                verify(clientRepository, times(1)).findByCpf(validRequest.cpf());
                verify(clientRepository, never()).findByEmail(anyString());
                verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        void shouldThrowDuplicateResourceException_WhenEmailAlreadyExists() {

                when(clientRepository.findByCpf(validRequest.cpf())).thenReturn(Optional.empty());
                when(clientRepository.findByEmail(validRequest.email())).thenReturn(Optional.of(client));

                DuplicateResourceException exception = assertThrows(
                                DuplicateResourceException.class,
                                () -> clientService.createClient(validRequest));

                assertTrue(
                                exception.getMessage().contains(
                                                "Cliente com email " + validRequest.email() + " já está cadastrado"));
                verify(clientRepository, times(1)).findByCpf(validRequest.cpf());
                verify(clientRepository, times(1)).findByEmail(validRequest.email());
                verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        void shouldUpdateClientSuccessfully_WhenDataIsValid() {

                Long clientId = 1L;
                ClientRequestDTO updateRequest = new ClientRequestDTO(
                                "Jane Doe",
                                "12345678901",
                                "jane.doe@example.com",
                                "11999999999",
                                null);

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
                when(clientRepository.findByEmail(updateRequest.email())).thenReturn(Optional.empty());
                when(clientRepository.findByCpf(updateRequest.cpf())).thenReturn(Optional.of(client));
                when(clientRepository.save(any(Client.class))).thenReturn(client);

                ClientResponseDTO response = clientService.updateClient(clientId, updateRequest);

                assertNotNull(response);
                verify(clientRepository, times(1)).findById(clientId);
                verify(clientRepository, times(1)).save(any(Client.class));
                verify(passwordEncoder, never()).encode(anyString()); // Password is null, should not encode
        }

        @Test
        void shouldUpdateClientPassword_WhenPasswordIsProvided() {

                Long clientId = 1L;
                ClientRequestDTO updateRequest = new ClientRequestDTO(
                                "John Doe",
                                "12345678901",
                                "john.doe@example.com",
                                "11987654321",
                                "newPassword123");

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
                when(clientRepository.findByEmail(updateRequest.email())).thenReturn(Optional.of(client));
                when(clientRepository.findByCpf(updateRequest.cpf())).thenReturn(Optional.of(client));
                when(passwordEncoder.encode(updateRequest.password())).thenReturn("newEncodedPassword");
                when(clientRepository.save(any(Client.class))).thenReturn(client);

                ClientResponseDTO response = clientService.updateClient(clientId, updateRequest);

                assertNotNull(response);
                verify(passwordEncoder, times(1)).encode(updateRequest.password());
                verify(clientRepository, times(1)).save(any(Client.class));
        }

        @Test
        void shouldNotUpdatePassword_WhenPasswordIsBlankOrNull() {

                Long clientId = 1L;
                ClientRequestDTO updateRequest = new ClientRequestDTO(
                                "John Doe",
                                "12345678901",
                                "john.doe@example.com",
                                "11987654321",
                                "");

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
                when(clientRepository.findByEmail(updateRequest.email())).thenReturn(Optional.of(client));
                when(clientRepository.findByCpf(updateRequest.cpf())).thenReturn(Optional.of(client));
                when(clientRepository.save(any(Client.class))).thenReturn(client);

                ClientResponseDTO response = clientService.updateClient(clientId, updateRequest);

                assertNotNull(response);
                verify(passwordEncoder, never()).encode(anyString());
                verify(clientRepository, times(1)).save(any(Client.class));
        }

        @Test
        void shouldThrowResourceNotFoundException_WhenClientNotFoundForUpdate() {

                Long invalidClientId = 999L;
                when(clientRepository.findById(invalidClientId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> clientService.updateClient(invalidClientId, validRequest));

                assertTrue(exception.getMessage().contains("Cliente com id " + invalidClientId + " não encontrado"));
                verify(clientRepository, times(1)).findById(invalidClientId);
                verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        void shouldThrowDuplicateResourceException_WhenUpdatingToExistingEmail() {

                Long clientId = 1L;
                Client anotherClient = new Client();
                anotherClient.setId(2L);
                anotherClient.setEmail("another@example.com");

                ClientRequestDTO updateRequest = new ClientRequestDTO(
                                "John Doe",
                                "12345678901",
                                "another@example.com",
                                "11987654321",
                                null);

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
                when(clientRepository.findByEmail(updateRequest.email())).thenReturn(Optional.of(anotherClient));

                DuplicateResourceException exception = assertThrows(
                                DuplicateResourceException.class,
                                () -> clientService.updateClient(clientId, updateRequest));

                assertTrue(
                                exception.getMessage().contains(
                                                "Cliente com email " + updateRequest.email() + " já está cadastrado"));
                verify(clientRepository, times(1)).findById(clientId);
                verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        void shouldThrowDuplicateResourceException_WhenUpdatingToExistingCpf() {

                Long clientId = 1L;
                Client anotherClient = new Client();
                anotherClient.setId(2L);
                anotherClient.setCpf("98765432100");

                ClientRequestDTO updateRequest = new ClientRequestDTO(
                                "John Doe",
                                "98765432100",
                                "john.doe@example.com",
                                "11987654321",
                                null);

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
                when(clientRepository.findByEmail(updateRequest.email())).thenReturn(Optional.of(client));
                when(clientRepository.findByCpf(updateRequest.cpf())).thenReturn(Optional.of(anotherClient));

                DuplicateResourceException exception = assertThrows(
                                DuplicateResourceException.class,
                                () -> clientService.updateClient(clientId, updateRequest));

                assertTrue(exception.getMessage()
                                .contains("Cliente com CPF " + updateRequest.cpf() + " já está cadastrado"));
                verify(clientRepository, times(1)).findById(clientId);
                verify(clientRepository, never()).save(any(Client.class));
        }

        @Test
        void shouldReturnLibrary_WhenClientExists() {
                Long clientId = 1L;
                Game game = new Game();
                game.setId(1L);
                game.setName("Game 1");
                game.setPrice(BigDecimal.TEN);
                game.setCategory(new com.gstech.PlayCommerce.model.Category()); // Mock category to avoid NPE in DTO

                client.setGames(Set.of(game));

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

                List<GameResponseDTO> library = clientService.getLibrary(clientId);

                assertNotNull(library);
                assertEquals(1, library.size());
                assertEquals("Game 1", library.get(0).name());
                verify(clientRepository, times(1)).findById(clientId);
        }

        @Test
        void shouldReturnDownloadLink_WhenClientOwnsGame() {
                Long clientId = 1L;
                Long gameId = 1L;
                String downloadLink = "http://download.com/game1";

                Game game = new Game();
                game.setId(gameId);
                game.setLinkDownload(downloadLink);

                client.setGames(Set.of(game));

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

                String link = clientService.getDownloadLink(clientId, gameId);

                assertEquals(downloadLink, link);
                verify(clientRepository, times(1)).findById(clientId);
        }

        @Test
        void shouldThrowResourceNotFoundException_WhenGameNotInLibrary() {
                Long clientId = 1L;
                Long gameId = 1L;

                client.setGames(Set.of()); // Empty library

                when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

                ResourceNotFoundException exception = assertThrows(
                                ResourceNotFoundException.class,
                                () -> clientService.getDownloadLink(clientId, gameId));

                assertTrue(exception.getMessage().contains("Jogo não encontrado na biblioteca do cliente"));
                verify(clientRepository, times(1)).findById(clientId);
        }
}
