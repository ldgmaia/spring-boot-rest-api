package com.example.api.domain.purchaseorders;

public record VendorResponseDTO(
        Vendor Vendor,
        String time
) {
    public record Vendor(
            double Balance,
            double BillRate,
            boolean Vendor1099,
            CurrencyRef CurrencyRef,
            double CostRate,
            String domain,
            boolean sparse,
            String Id,
            String SyncToken,
            MetaData MetaData,
            String DisplayName,
            String PrintOnCheckName,
            boolean Active,
            String V4IDPseudonym
    ) {
    }

    public record CurrencyRef(
            String value,
            String name
    ) {
    }

    public record MetaData(
            String CreateTime,
            String LastUpdatedTime
    ) {
    }
}
