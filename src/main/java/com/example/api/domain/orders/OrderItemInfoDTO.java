package com.example.api.domain.orders;

// I didn't check this file

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderItemInfoDTO(
        Long id,
        String sku,
        String name,
        Integer quantity,
        BigDecimal unitPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public OrderItemInfoDTO(OrderItem orderItem) {
        this(
                orderItem.getId(),
                orderItem.getSku(),
                orderItem.getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getCreatedAt(),
                orderItem.getUpdatedAt()
        );
    }
}
