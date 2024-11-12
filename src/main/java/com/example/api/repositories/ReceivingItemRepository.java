package com.example.api.repositories;

import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.receivings.ReceivingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceivingItemRepository extends JpaRepository<ReceivingItem, Long> {

    @Query("""
            SELECT COALESCE(SUM(ri.quantityAlreadyReceived), 0)
            FROM ReceivingItem ri
            WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId
            """)
    Long findSumAlreadyReceivedByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    @Query("""
            SELECT COALESCE(SUM(ri.quantityAlreadyReceived), 0)
            FROM ReceivingItem ri
            JOIN ri.purchaseOrderItem poi
            WHERE poi.purchaseOrder.id = :purchaseOrderId
            """)
    Long findSumAlreadyReceivedByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);

    @Query("""
                SELECT r
                FROM ReceivingItem r
                WHERE r.purchaseOrderItem.id = :purchaseOrderItemId
            """)
    List<ReceivingItem> findByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    Page<ReceivingItem> findByReceiving_TypeAndStatusInAndQuantityAlreadyReceivedGreaterThanOrderByUpdatedAtDesc(ReceivingType type, String[] statuses, Long quantityAlreadyReceived, Pageable pageable);

    List<ReceivingItem> findByReceiving_TypeAndQuantityAlreadyReceivedGreaterThanAndStatusInOrderByCreatedAtDesc(ReceivingType type, Long quantityAlreadyReceived, String[] statuses);

    List<ReceivingItem> findByReceiving_TypeAndPurchaseOrderItem_PurchaseOrder_PoNumberAndStatusInOrderByCreatedAtDesc(ReceivingType type, String poNumber, String[] statuses);

    List<ReceivingItem> findByInventoryItems_SerialNumberAndReceiving_TypeOrderByCreatedAtDesc(String serialNumber, ReceivingType receivingType);

    List<ReceivingItem> findByReceiving_TypeAndReceiving_IdAndStatusInOrderByCreatedAtDesc(ReceivingType type, Long id, String[] statuses);

    List<ReceivingItem> findByReceiving_TypeAndStatusInAndDescriptionContainingOrderByCreatedAtDesc(ReceivingType type, String[] statuses, String description);

    List<ReceivingItem> findByReceiving_TypeAndStatusInAndReceiving_TrackingLadingOrderByCreatedAtDesc(ReceivingType type, String[] statuses, String trackingLading);

    List<ReceivingItem> findByReceiving_TypeAndStatusInAndReceiving_Supplier_IdOrderByCreatedAtDesc(ReceivingType type, String[] statuses, Long id);

}
