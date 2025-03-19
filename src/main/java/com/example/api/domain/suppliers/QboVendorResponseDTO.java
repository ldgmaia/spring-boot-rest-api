package com.example.api.domain.suppliers;

import jakarta.annotation.Nullable;

public record QboVendorResponseDTO(
        Vendor Vendor,
        String time
) {
    public record Vendor(
            BillAddr BillAddr,
            TermRef TermRef,
            double Balance,
            double BillRate,
            String AcctNum,
            boolean Vendor1099,
            CurrencyRef CurrencyRef,
            double CostRate,
            String domain,
            boolean sparse,
            String Id,
            String SyncToken,
            MetaData MetaData,
            String CompanyName,
            String DisplayName,
            String PrintOnCheckName,
            boolean Active,
            String V4IDPseudonym,
            PrimaryPhone PrimaryPhone,
            Fax Fax,

            @Nullable
            PrimaryEmailAddr PrimaryEmailAddr,
            WebAddr WebAddr
    ) {
    }

    public record BillAddr(
            String Id,
            String Line1,
            String Line2,
            String Line3,
            String City,
            String Country,
            String CountrySubDivisionCode,
            String PostalCode,
            String Lat,
            String Long
    ) {
    }

    public record TermRef(
            String value
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

    public record PrimaryPhone(
            String FreeFormNumber
    ) {
    }

    public record Fax(
            String FreeFormNumber
    ) {
    }

    public record PrimaryEmailAddr(
            String Address
    ) {
    }

    public record WebAddr(
            String URI
    ) {
    }
}
