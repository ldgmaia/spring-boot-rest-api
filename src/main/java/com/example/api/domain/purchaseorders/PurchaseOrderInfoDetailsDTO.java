package com.example.api.domain.purchaseorders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseOrderInfoDetailsDTO(
        Long id,
        String status,
        String poNumber,
        String currency,
        BigDecimal total,
        String receivingStatus,
        LocalDateTime lastReceivedAt

//        SupplierInfoDTO supplier,
//        List<PurchaseOrderItemInfoDTO> purchaseOrderItems
) {
}


