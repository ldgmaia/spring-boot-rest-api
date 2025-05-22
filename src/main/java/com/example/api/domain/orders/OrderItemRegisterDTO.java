package com.example.api.domain.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderItemRegisterDTO(
        Order order,
        Long orderItemId,
        String lineItemKey,
        String sku,
        String name,
        String imageUrl,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal taxAmount,
        BigDecimal shippingAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
