package com.example.api.domain.purchaseorders;

import com.example.api.domain.ValidationException;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoReceivedDTO;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItemRegisterDTO;
import com.example.api.domain.suppliers.Supplier;
import com.example.api.domain.suppliers.SupplierRegisterDTO;
import com.example.api.repositories.PurchaseOrderItemRepository;
import com.example.api.repositories.PurchaseOrderRepository;
import com.example.api.repositories.ReceivingItemRepository;
import com.example.api.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    public PurchaseOrderInfoDTO register(PurchaseOrderRequestDTO data) {

        var supplier = supplierRepository.findById(1L).orElse(null);
        if (supplier == null) {// Create Supplier
            supplier = new Supplier(new SupplierRegisterDTO(
                    "Vendor?.DisplayName",
                    "Vendor.PrimaryPhone?.FreeFormNumber",
                    "Vendor.PrimaryEmailAddr?.Address",
                    "Vendor?.CompanyName",
                    "Vendor.BillAddr?.Line1",
                    "Vendor.BillAddr?.Line2",
                    "Vendor.BillAddr?.Line3",
                    "Vendor.BillAddr?.City",
                    "Vendor.BillAddr?.CountrySubDivisionCode",
                    "Vendor.BillAddr?.PostalCode",
                    "Vendor.BillAddr?.Country"
            ));
            supplierRepository.save(supplier);
        }

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(data.qbo_created_at());
        // Convert to ZonedDateTime in the "America/Toronto" time zone
        ZonedDateTime torontoZonedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("America/Toronto")).toOffsetDateTime().toZonedDateTime();
        // Extract the LocalDateTime in the Toronto time zone
        LocalDateTime torontoLocalDateTime = torontoZonedDateTime.toLocalDateTime();

        var purchaseOrder = new PurchaseOrder(new PurchaseOrderRegisterDTO(
                data.status(),
                data.po_number(),
                data.currency(),
                BigDecimal.valueOf(Long.parseLong(data.total())),
                Long.valueOf(data.qbo_id()),
                OffsetDateTime.parse(data.qbo_created_at()).toLocalDateTime(),
                OffsetDateTime.parse(data.qbo_updated_at()).toLocalDateTime(),
                supplier,
                "watchingPo"
        ));
        purchaseOrderRepository.save(purchaseOrder);

        // Create PO Items
        for (int i = 0; i < 5; i++) {
            var poi = new PurchaseOrderItem(new PurchaseOrderItemRegisterDTO(
                    "name " + i,
                    "description " + i,
                    10L,
                    new BigDecimal(10),
                    new BigDecimal(10),
                    1L,
                    1L,
                    purchaseOrder
            ));
            purchaseOrderItemRepository.save(poi);
        }

        var items = purchaseOrderItemRepository.findAllByPurchaseOrderId(purchaseOrder.getId());

        return new PurchaseOrderInfoDTO(purchaseOrder, supplier, items);
    }

    public PurchaseOrderInfoReceivedDTO show(Long id) {
        var purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() -> new ValidationException("Purchase order not found"));
        var supplier = purchaseOrder.getSupplier();

        var items = purchaseOrderItemRepository.findAllByPurchaseOrderId(purchaseOrder.getId())
                .stream()
                .map(poi -> {
                    var receivingItem = receivingItemRepository.findByPurchaseOrderItemId(poi.id());
                    if (receivingItem != null) {
                        Long quantityReceived = receivingItemRepository.findQuantityReceivedByPurchaseOrderItemId(poi.id());
                        return new PurchaseOrderItemInfoReceivedDTO(purchaseOrderItemRepository.getReferenceById(poi.id()), quantityReceived != null ? quantityReceived : 0L, receivingItem);  // Include quantityReceived
                    } else {
                        return new PurchaseOrderItemInfoReceivedDTO(purchaseOrderItemRepository.getReferenceById(poi.id()), 0L, receivingItem);  // Include quantityReceived
                    }
                })
                .toList();


        return new PurchaseOrderInfoReceivedDTO(purchaseOrder, supplier, items);
    }
}
