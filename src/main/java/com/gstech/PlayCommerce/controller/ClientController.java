package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.ClientRequestDTO;
import com.gstech.PlayCommerce.dto.ClientResponseDTO;
import com.gstech.PlayCommerce.dto.UpdateRequestDTO;
import com.gstech.PlayCommerce.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO requestDTO) {
        ClientResponseDTO responseDTO = clientService.createClient(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequestDTO requestDTO) {
        ClientResponseDTO responseDTO = clientService.updateClient(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}

