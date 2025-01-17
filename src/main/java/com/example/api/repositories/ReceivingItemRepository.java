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

    Page<ReceivingItem> findByReceivingTypeAndStatusInAndQuantityAlreadyReceivedGreaterThanOrderByUpdatedAtDesc(ReceivingType type, String[] statuses, Long quantityAlreadyReceived, Pageable pageable);

    List<ReceivingItem> findByReceivingTypeAndQuantityAlreadyReceivedGreaterThanAndStatusInOrderByCreatedAtDesc(ReceivingType type, Long quantityAlreadyReceived, String[] statuses);

    List<ReceivingItem> findByReceivingTypeAndPurchaseOrderItemPurchaseOrderPoNumberAndStatusInOrderByCreatedAtDesc(ReceivingType type, String poNumber, String[] statuses);

    List<ReceivingItem> findByInventoryItemsSerialNumberAndReceivingTypeOrderByCreatedAtDesc(String serialNumber, ReceivingType receivingType);

    List<ReceivingItem> findByReceivingTypeAndReceivingIdAndStatusInOrderByCreatedAtDesc(ReceivingType type, Long id, String[] statuses);

    List<ReceivingItem> findByReceivingTypeAndStatusInAndDescriptionContainingOrderByCreatedAtDesc(ReceivingType type, String[] statuses, String description);

    List<ReceivingItem> findByReceivingTypeAndStatusInAndReceivingTrackingLadingOrderByCreatedAtDesc(ReceivingType type, String[] statuses, String trackingLading);

    List<ReceivingItem> findByReceivingTypeAndStatusInAndReceivingSupplierIdOrderByCreatedAtDesc(ReceivingType type, String[] statuses, Long id);

}
