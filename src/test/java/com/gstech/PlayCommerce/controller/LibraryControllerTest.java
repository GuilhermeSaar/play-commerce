package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.GameResponseDTO;
import com.gstech.PlayCommerce.exception.ResourceNotFoundException;
import com.gstech.PlayCommerce.service.ClientService;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private LibraryController libraryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
    }

    @Test
    void shouldReturnLibrary_WhenClientExists() throws Exception {
        Long clientId = 1L;
        GameResponseDTO gameDTO = new GameResponseDTO(
                1L, "Game 1", "Description", "Dev", "Pub",
                LocalDate.now(), BigDecimal.TEN, "18+", 1L);

        when(clientService.getLibrary(clientId)).thenReturn(List.of(gameDTO));

        mockMvc.perform(get("/library/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Game 1"));
    }
}
