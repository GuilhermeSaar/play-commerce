package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.security.MyUserPrincipal;

import com.gstech.PlayCommerce.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final ClientService clientService;

    public LibraryController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<List<GameResponseDTO>> getLibrary(@PathVariable Long clientId) {
        List<GameResponseDTO> library = clientService.getLibrary(clientId);
        return ResponseEntity.ok(library);
    }


    @GetMapping("/my-library")
    public ResponseEntity<List<GameResponseDTO>> getMyLibrary(@AuthenticationPrincipal MyUserPrincipal userPrincipal) {
        List<GameResponseDTO> library = clientService.getLibrary(userPrincipal.getUser().getId());
        return ResponseEntity.ok(library);
    }
}
