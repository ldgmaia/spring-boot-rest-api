package com.example.api.domain.orders;

import com.example.api.domain.customers.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderRegisterDTO(
        Long orderId,
        String orderNumber,
        String orderKey,
        LocalDateTime orderDate,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        LocalDateTime paymentDate,
        LocalDateTime shipByDate,
        String orderStatus,
        BigDecimal orderTotal,
        BigDecimal amountPaid,
        BigDecimal taxAmount,
        BigDecimal shippingAmount,
        String paymentMethod,
        String carrierCode,
        String trackingNumber,
        String confirmation,
        String requestedShippingService,
        String serviceCode,
        String packageCode,
        String customerNotes,
        String internalNotes,
        Customer customer
) {
}
