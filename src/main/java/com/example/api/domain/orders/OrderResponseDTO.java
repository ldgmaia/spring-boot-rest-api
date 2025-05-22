package com.example.api.domain.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderId,
        String orderNumber,
        String orderKey,
        LocalDateTime orderDate,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        LocalDateTime paymentDate,
        LocalDateTime shipByDate,
        String orderStatus,
        Long customerId,
        String customerUsername,
        String customerEmail,
        Address billTo,
        Address shipTo,
        List<Item> items,
        BigDecimal orderTotal,
        BigDecimal amountPaid,
        BigDecimal taxAmount,
        BigDecimal shippingAmount,
        String customerNotes,
        String internalNotes,
        Boolean gift,
        String giftMessage,
        String paymentMethod,
        String requestedShippingService,
        String carrierCode,
        String serviceCode,
        String packageCode,
        String confirmation,
        LocalDateTime shipDate,
        LocalDateTime holdUntilDate,
        AdvancedOptions advancedOptions,
        List<Long> tagIds,
        Long userId,
        Boolean externallyFulfilled,
        String externallyFulfilledBy,
        Long externallyFulfilledById,
        String externallyFulfilledByName,
        String labelMessages
) {

    public record Address(
            String name,
            String company,
            String street1,
            String street2,
            String street3,
            String city,
            String state,
            String postalCode,
            String country,
            String phone,
            Boolean residential,
            String addressVerified
    ) {
    }

    public record Item(
            Long orderItemId,
            String lineItemKey,
            String sku,
            String name,
            String imageUrl,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal taxAmount,
            BigDecimal shippingAmount,
            String warehouseLocation,
            Long productId,
            String fulfillmentSku,
            Boolean adjustment,
            String upc,
            LocalDateTime createDate,
            LocalDateTime modifyDate
    ) {
    }

//    public record Weight(
//            BigDecimal value,
//            String units,
//            Long WeightUnits
//    ) {
//    }

//    public record Dimensions(
//            String units,
//            BigDecimal length,
//            BigDecimal width,
//            BigDecimal height
//    ) {
//    }

//    public record InsuranceOptions(
//            String provider,
//            Boolean insureShipment,
//            BigDecimal insuredValue
//    ) {
//    }

//    public record InternationalOptions(
//            String contents,
//            List<CustomsItem> customsItems,
//            String nonDelivery
//    ) {
//    }

//    public record CustomsItem(
//            Long customsItemId,
//            String description,
//            Long quantity,
//            BigDecimal value,
//            String harmonizedTariffCode,
//            String countryOfOrigin
//    ) {
//    }

    public record AdvancedOptions(
            int warehouseId,
            Boolean nonMachinable,
            Boolean saturdayDelivery,
            Boolean containsAlcohol,
            Boolean mergedOrSplit,
            List<Long> mergedIds,
            Long parentId,
            Long storeId,
            String customField1,
            String customField2,
            String customField3,
            String source,
            String billToParty,
            String billToAccount,
            String billToPostalCode,
            String billToCountryCode,
            Long billToMyOtherAccount
    ) {
    }
}
