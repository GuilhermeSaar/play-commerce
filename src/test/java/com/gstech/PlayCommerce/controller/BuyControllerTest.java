package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.BuyResponseDTO;
import com.gstech.PlayCommerce.exception.GlobalExceptionHandler;
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
import static org.mockito.Mockito.verifyNoInteractions;
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
        mockMvc = MockMvcBuilders.standaloneSetup(buyController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldCreateBuy_WhenRequestIsValid() throws Exception {
        BuyResponseDTO response = new BuyResponseDTO(1L, 1L, List.of(), BigDecimal.TEN, LocalDateTime.now(), null);

        when(buyService.createBuy(any())).thenReturn(response);

        mockMvc.perform(post("/api/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"clientId\":1,\"status\":\"PENDING\",\"items\":[{\"gameId\":1,\"quantity\":1}],\"payment\":{\"paymentMethod\":\"PIX\",\"paymentStatus\":\"PENDENTE\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.clientId").value("Cliente é obrigatório"));

        verifyNoInteractions(buyService);
    }

    @Test
    void shouldReturnBadRequest_WhenServiceRejectsBusinessRule() throws Exception {
        when(buyService.createBuy(any())).thenThrow(new IllegalArgumentException("Compra inválida"));

        mockMvc.perform(post("/api/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"clientId\":1,\"status\":\"PENDING\",\"items\":[{\"gameId\":1,\"quantity\":1}],\"payment\":{\"paymentMethod\":\"PIX\",\"paymentStatus\":\"PENDENTE\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Compra inválida"));
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
