package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.GameRequestDTO;
import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }

    @GetMapping
    public ResponseEntity<java.util.List<GameResponseDTO>> findAllGames() {
        var games = gameService.findAllGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> findGameById(@PathVariable Long id) {
        var game = gameService.findGameById(id);
        return ResponseEntity.ok(game);
    }
}
