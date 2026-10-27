package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.BuyRequest;
import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.service.BuyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/buy")
public class BuyController {

    private final BuyService buyService;

    public BuyController(BuyService buyService) {
        this.buyService = buyService;
    }

    @PostMapping
    public ResponseEntity<BuyResponseDTO> createBuy(@Valid @RequestBody BuyRequest request,
            UriComponentsBuilder uriBuilder) {
        var response = buyService.createBuy(request);
        var uri = uriBuilder.path("/api/buy/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/history/{clientId}")
    public ResponseEntity<List<BuyResponseDTO>> getBuysByClient(@PathVariable Long clientId) {
        List<BuyResponseDTO> buys = buyService.getBuysByClient(clientId);
        return ResponseEntity.ok(buys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyResponseDTO> getBuyById(@PathVariable Long id) {
        BuyResponseDTO buy = buyService.getBuyById(id);
        return ResponseEntity.ok(buy);
    }
}
