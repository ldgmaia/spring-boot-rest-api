package com.example.api.domain.purchaseorders;

import java.util.List;

public record PurchaseOrderResponseDTO(
        PurchaseOrder PurchaseOrder,
        String time
) {

    public record PurchaseOrder(
            VendorAddr VendorAddr,
            ShipAddr ShipAddr,
            String EmailStatus,
            String POStatus,
            String domain,
            boolean sparse,
            String Id,
            String SyncToken,
            MetaData MetaData,
            List<Object> CustomField,
            String DocNumber,
            String TxnDate,
            CurrencyRef CurrencyRef,
            String PrivateNote,
            List<LinkedTxn> LinkedTxn,
            List<Line> Line,
            VendorRef VendorRef,
            APAccountRef APAccountRef,
            double TotalAmt
    ) {
    }

    public record VendorAddr(
            String Id,
            String Line1
    ) {
    }

    public record ShipAddr(
            String Id,
            String Line1,
            String Line2,
            String Line3
    ) {
    }

    public record MetaData(
            String CreateTime,
            LastModifiedByRef LastModifiedByRef,
            String LastUpdatedTime
    ) {
    }

    public record LastModifiedByRef(
            String value
    ) {
    }

    public record CurrencyRef(
            String value,
            String name
    ) {
    }

    public record LinkedTxn(
            String TxnId,
            String TxnType
    ) {
    }

    public record Line(
            String Id,
            int LineNum,
            String Description,
            double Amount,
            double Received,
            String DetailType,
            ItemBasedExpenseLineDetail ItemBasedExpenseLineDetail,
            AccountBasedExpenseLineDetail AccountBasedExpenseLineDetail
    ) {
    }

    public record ItemBasedExpenseLineDetail(
            String BillableStatus,
            ItemRef ItemRef,
            double UnitPrice,
            double Qty,
            TaxCodeRef TaxCodeRef
    ) {
    }

    public record AccountBasedExpenseLineDetail(
            AccountRef AccountRef,
            String BillableStatus,
            TaxCodeRef TaxCodeRef
    ) {
    }

    public record ItemRef(
            String value,
            String name
    ) {
    }

    public record TaxCodeRef(
            String value
    ) {
    }

    public record AccountRef(
            String value,
            String name
    ) {
    }

    public record VendorRef(
            String value,
            String name
    ) {
    }

    public record APAccountRef(
            String value,
            String name
    ) {
    }
}
