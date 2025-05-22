package com.example.api.domain.orders;

import java.util.List;

public record OrderListResponseDTO(
        List<OrderResponseDTO> orders,
        Long total,
        Long page,
        Long pages
) {
}
