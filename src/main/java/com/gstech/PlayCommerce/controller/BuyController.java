package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.BuyRequestDTO;
import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.service.BuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/buy")
public class BuyController {

    @Autowired
    private BuyService buyService;

    @PostMapping
    public ResponseEntity<BuyResponseDTO> createBuy(@RequestBody BuyRequestDTO request,
            UriComponentsBuilder uriBuilder) {
        var response = buyService.createBuy(request);
        var uri = uriBuilder.path("/buy/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
