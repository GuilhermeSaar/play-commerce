package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameResponseDTO> createGame(@Valid @RequestBody GameRequestDTO request) {
        var createdGame = gameService.createGame(request);
        return ResponseEntity.ok(createdGame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDTO> updateGame(
            @Valid @RequestBody GameRequestDTO request,
            @PathVariable Long id
    ) {
        var updatedGame = gameService.updateGame(request, id);
        return ResponseEntity.ok(updatedGame);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
