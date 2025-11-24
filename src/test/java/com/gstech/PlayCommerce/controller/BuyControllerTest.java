package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.BuyGameResponseDTO;
import com.gstech.PlayCommerce.dto.BuyRequestDTO;
import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.dto.PaymentRequestDTO;
import com.gstech.PlayCommerce.dto.PaymentResponseDTO;
import com.gstech.PlayCommerce.model.enums.PaymentStatusType;
import com.gstech.PlayCommerce.model.enums.PaymentType;
import com.gstech.PlayCommerce.service.BuyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BuyControllerTest {

    @Mock
    private BuyService buyService;

    @InjectMocks
    private BuyController buyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(buyController).build();
    }

    @Test
    void shouldCreateBuy_WhenRequestIsValid() throws Exception {
        BuyRequestDTO request = new BuyRequestDTO(1L, List.of(1L),
                new PaymentRequestDTO(PaymentType.PIX, PaymentStatusType.APROVADO));
        BuyResponseDTO response = new BuyResponseDTO(1L, 1L, List.of(), BigDecimal.TEN, LocalDateTime.now(), null);

        when(buyService.createBuy(any(BuyRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"clientId\": 1, \"gameIds\": [1], \"payment\": {\"type\": \"PIX\", \"status\": \"APROVADO\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnBuysByClient_WhenClientHasBuys() throws Exception {
        Long clientId = 1L;
        BuyResponseDTO buy = new BuyResponseDTO(1L, clientId, List.of(), BigDecimal.TEN, LocalDateTime.now(), null);

        when(buyService.getBuysByClient(clientId)).thenReturn(List.of(buy));

        mockMvc.perform(get("/api/buy/history/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldReturnBuyById_WhenBuyExists() throws Exception {
        Long buyId = 1L;
        BuyResponseDTO buy = new BuyResponseDTO(buyId, 1L, List.of(), BigDecimal.TEN, LocalDateTime.now(), null);

        when(buyService.getBuyById(buyId)).thenReturn(buy);

        mockMvc.perform(get("/api/buy/{id}", buyId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(buyId));
    }
}
