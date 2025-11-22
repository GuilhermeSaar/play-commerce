package com.gstech.PlayCommerce.dto;

import java.util.List;

public record BuyRequestDTO(Long clientId, List<Long> gameIds) {
}
