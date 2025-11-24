package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.service.ClientService;
import com.gstech.PlayCommerce.security.MyUserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/{clientId}/download/{gameId}")
    public ResponseEntity<Map<String, String>> downloadGame(@PathVariable Long clientId, @PathVariable Long gameId) {
        String link = clientService.getDownloadLink(clientId, gameId);
        return ResponseEntity.ok(Map.of("linkDownload", link));
    }

    @GetMapping("/my-library")
    public ResponseEntity<List<GameResponseDTO>> getMyLibrary(@AuthenticationPrincipal MyUserPrincipal userPrincipal) {
        List<GameResponseDTO> library = clientService.getLibrary(userPrincipal.getUser().getId());
        return ResponseEntity.ok(library);
    }
}
