package com.example.api.repositories;

import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceivingItemRepository extends JpaRepository<ReceivingItem, Long> {

    @Query("SELECT COALESCE(SUM(ri.quantityAlreadyReceived), 0) FROM ReceivingItem ri WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId")
    Long findQuantityAlreadyReceivedByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    @Modifying
    @Query("UPDATE ReceivingItem ri SET ri.status = :status WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId")
    void updateStatusByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId, @Param("status") String status);

    @Query("SELECT COALESCE(SUM(ri.quantityAlreadyReceived), 0) FROM ReceivingItem ri WHERE ri.purchaseOrderItem.id = :purchaseOrderItemId")
    Long findTotalReceivedQuantityByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    @Query("""
                SELECT r 
                FROM ReceivingItem r 
                WHERE r.purchaseOrderItem.id = :purchaseOrderItemId
            """)
    List<ReceivingItem> findByPurchaseOrderItemId(@Param("purchaseOrderItemId") Long purchaseOrderItemId);

    @Query("""
            SELECT new com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityAlreadyReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            JOIN ri.receiving r
            WHERE r.type = 'PO'
                AND ri.quantityAlreadyReceived > 0
                AND ri.status in (:status)
                order by ri.createdAt DESC
            """)
    Page<ReceivingItemAssessmentListDTO> findReceivingsByStatus(Pageable pageable, @Param("status") String[] status);

    @Query("""
            SELECT new com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityAlreadyReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            JOIN ri.receiving r
            WHERE r.type = 'PO'
                AND po.poNumber = :poNumber
                AND ri.status in (:status)
                order by ri.createdAt DESC
            """)
    List<ReceivingItemAssessmentListDTO> findReceivedItemsByPurchaseOrderNumber(@Param("status") String[] status, @Param("poNumber") String poNumber);


    @Query("""
            SELECT new com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityAlreadyReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN InventoryItem ii on ri.id = ii.receivingItem.id
            JOIN ri.receiving r
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            WHERE r.type = 'PO'
                AND ii.serialNumber LIKE %:serialNumber%
                order by ri.createdAt DESC
            """)
    List<ReceivingItemAssessmentListDTO> findReceivedItemsBySerialNumber(@Param("serialNumber") String serialNumber);
    //List<ReceivingAssessmentListDTO> findReceivedItemsBySerialNumber(@Param("status") String[] status, @Param("serialNumber") String serialNumber);

    @Query("""
            SELECT new com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityAlreadyReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN ri.receiving r
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            WHERE r.type = 'PO'
                AND ri.status in (:status)
                AND r.id = :receivingId
                order by ri.createdAt DESC
            """)
    List<ReceivingItemAssessmentListDTO> findReceivedItemsByReceivingId(@Param("status") String[] status, @Param("receivingId") Long receivingId);

    @Query("""
            SELECT new com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityAlreadyReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN ri.receiving r
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            WHERE r.type = 'PO'
                AND ri.status in (:status)
                AND ri.description LIKE  %:description%
                order by ri.createdAt DESC
            """)
    List<ReceivingItemAssessmentListDTO> findReceivedItemsByDescription(@Param("status") String[] status, @Param("description") String description);

    @Query("""
            SELECT new com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityAlreadyReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN ri.receiving r
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            WHERE r.type = 'PO'
                AND ri.status in (:status)
                AND r.trackingLading = :trackingLading
                order by ri.createdAt DESC
            """)
    List<ReceivingItemAssessmentListDTO> findReceivedItemsByTrackinglading(@Param("status") String[] status, @Param("trackingLading") String trackingLading);


    @Query("""
            SELECT new com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO(
                ri.id, po.poNumber, ri.description, ri.status, poi.quantityOrdered, ri.quantityAlreadyReceived, s.name, ri.createdAt
            )
            FROM ReceivingItem ri
            JOIN ri.receiving r
            JOIN ri.purchaseOrderItem poi
            JOIN poi.purchaseOrder po
            JOIN po.supplier s
            WHERE r.type = 'PO'
                AND ri.status in (:status)
                AND r.id = :supplierId
                order by ri.createdAt DESC
            """)
    List<ReceivingItemAssessmentListDTO> findReceivedItemsBySupplierId(@Param("status") String[] status, @Param("supplierId") Long supplierId);

}
