package com.example.api.domain.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// I didn't check this file

public record OrderInfoDTO(
        Long id,
        String orderNumber,
        String orderKey,
        LocalDateTime orderDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime paymentDate,
        LocalDateTime shipByDate,
        String orderStatus,
        Long customerId,
        String customerUsername,
        String customerEmail,
        CustomerAddressDTO billToAddress,
        CustomerAddressDTO shipToAddress,
        BigDecimal orderTotal,
        BigDecimal amountPaid,
        BigDecimal taxAmount,
        BigDecimal shippingAmount,
        String paymentMethod,
        String requestedShippingService,
        String carrierCode,
        String serviceCode,
        String packageCode,
        String confirmation,
        String source,
        List<OrderItemInfoDTO> orderItems
) {
//    public static OrderInfoDTO fromOrder(Order order) {
//        if (order == null) {
//            return null;
//        }
//
//        List<OrderItemInfoDTO> orderItemDTOs = order.getOrderItems() != null
//                ? order.getOrderItems().stream()
//                .map(OrderItemInfoDTO::new)
//                .toList()
//                : List.of();
//
//        InsuranceOptionInfoDTO insuranceDTO = order.getInsuranceOption() != null
//                ? new InsuranceOptionInfoDTO(order.getInsuranceOption())
//                : null;
//
//        InternationalOptionInfoDTO internationalDTO = order.getInternationalOption() != null
//                ? new InternationalOptionInfoDTO(order.getInternationalOption())
//                : null;
//
//        CustomerAddressDTO billTo = null;
//        if (order.getBillToCustomer() != null) {
//            billTo = new CustomerAddressDTO(
//                    order.getBillToCustomer().getName(),
//                    null, // company
//                    null, // street1
//                    null, // street2
//                    null, // street3
//                    null, // city
//                    null, // state
//                    null, // postalCode
//                    null, // country
//                    order.getBillToCustomer().getPhone(),
//                    null, // residential
//                    null  // addressVerified
//            );
//        }
//
//        CustomerAddressDTO shipTo = null;
//        if (order.getShipToCustomer() != null) {
//
//            shipTo = new CustomerAddressDTO(
//                    order.getShipToCustomer().getName(),
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    order.getShipToCustomer().getPhone(),
//                    null,
//                    null
//            );
//        }
//
//        return new OrderInfoDTO(
//                order.getId(),
//                order.getOrderNumber(),
//                order.getOrderKey(),
//                order.getOrderDate(),
//                order.getCreatedAt(),
//                order.getUpdatedAt(),
//                order.getPaymentDate(),
//                order.getShipByDate(),
//                order.getOrderStatus(),
//                order.getCustomer() != null ? order.getCustomer().getId() : null,
//                order.getCustomer() != null ? order.getCustomer().getUsername() : null,
//                order.getCustomer() != null ? order.getCustomer().getEmail() : null,
//                billTo,
//                shipTo,
//                order.getOrderTotal(),
//                order.getAmountPaid(),
//                order.getTaxAmount(),
//                order.getShippingAmount(),
//                order.getPaymentMethod(),
//                order.getRequestedShippingService(),
//                order.getCarrierCode(),
//                order.getServiceCode(),
//                order.getPackageCode(),
//                order.getConfirmation(),
//                null,
//                orderItemDTOs,
//                insuranceDTO,
//                internationalDTO
//        );
//    }
}
