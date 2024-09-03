package com.example.api.domain.purchaseorders;

import com.example.api.domain.ValidationException;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.purchaseorderitems.PurchaseOrderItemRegisterDTO;
import com.example.api.domain.suppliers.Supplier;
import com.example.api.domain.suppliers.SupplierRegisterDTO;
import com.example.api.repositories.PurchaseOrderItemRepository;
import com.example.api.repositories.PurchaseOrderRepository;
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

    public PurchaseOrderInfoDTO register(PurchaseOrderRequestDTO data) {
//        validators.forEach(v -> v.validate(data));

        // Create Supplier
        var supplier = new Supplier(new SupplierRegisterDTO(
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

        System.out.println("supplier ID " + supplier.getId());

        // Create Purachse Order

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(data.qbo_created_at());
        // Convert to ZonedDateTime in the "America/Toronto" time zone
        ZonedDateTime torontoZonedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.of("America/Toronto")).toOffsetDateTime().toZonedDateTime();
        // Extract the LocalDateTime in the Toronto time zone
        LocalDateTime torontoLocalDateTime = torontoZonedDateTime.toLocalDateTime();

        var purchaseOrder = new PurchaseOrder(new PurchaseOrderRegisterDTO(
                "status",
                "poNumber",
                "currency",
                BigDecimal.valueOf(10),
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                supplier.getId(),
                "receivingStatus",
                "watchingPo"
        ));
        purchaseOrderRepository.save(purchaseOrder);

        // Create PO Items

        var poi = new PurchaseOrderItem(new PurchaseOrderItemRegisterDTO(
                "name",
                "description",
                1L,
                new BigDecimal(10),
                new BigDecimal(10),
                1L,
                1L,
                purchaseOrder
        ));

        purchaseOrderItemRepository.save(poi);

        var items = purchaseOrderItemRepository.findAllByPurchaseOrderId(purchaseOrder.getId());

        return new PurchaseOrderInfoDTO(purchaseOrder, supplier, items);
//        return new PurchaseOrderInfoDTO(1L, "123", "a", torontoLocalDateTime, "asdasd");

    }

    public PurchaseOrderInfoDTO show(Long id) {

        var purchaseOrder = purchaseOrderRepository.findById(id).orElseThrow(() -> new ValidationException("Purchase order not found"));

        var supplier = supplierRepository.getReferenceById(purchaseOrder.getSuppliersId());

        var items = purchaseOrderItemRepository.findAllByPurchaseOrderId(purchaseOrder.getId());

        return new PurchaseOrderInfoDTO(purchaseOrder, supplier, items);
    }

}
